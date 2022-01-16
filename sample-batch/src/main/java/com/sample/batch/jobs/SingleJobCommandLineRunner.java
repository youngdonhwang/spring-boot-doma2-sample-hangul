package com.sample.batch.jobs;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.batch.core.*;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.StringUtils;

import com.sample.common.util.FileUtils;

import lombok.Getter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 단일 Job만을 처리하는 CommandLineJobRunner
 */
@Slf4j
public class SingleJobCommandLineRunner implements CommandLineRunner, ApplicationEventPublisherAware {

    @Value("${application.processFileLocation:#{systemProperties['java.io.tmpdir']}}")
    private String processFileLocation;

    public static final String JOB_PARAMETER_JOB_NAME = "--job";

    @Getter
    @Autowired
    JobParametersConverter converter;

    @Getter
    @Autowired
    JobLauncher jobLauncher;

    @Getter
    @Autowired
    JobExplorer jobExplorer;

    @Getter
    @Autowired
    Collection<Job> jobs = Collections.emptySet();

    @Getter
    ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void run(String... args) throws JobExecutionException, IOException {
        val jobParameters = getJobParameters(args);

        if (jobParameters == null) {
            throw new IllegalArgumentException("인수가 지정되지 않았습니다.");
        }

        // 인수의 jobName으로 Job인스턴스를 추출
        String targetJobName = jobParameters.getString(JOB_PARAMETER_JOB_NAME);

        Optional<Job> jobOpt = jobs.stream().filter(s -> s.getName().equalsIgnoreCase(targetJobName)).findFirst();
        Job job = jobOpt
                .orElseThrow(() -> new IllegalArgumentException("지정된 Job이 없습니다. [jobName=" + targetJobName + "]"));

        // 이중 기동 방지 파일을 작성한다.
        createProcessFile(processFileLocation, targetJobName);

        // Job을 실행한다
        val status = execute(job, jobParameters);

        // 이중 기동 방지 파일을 삭제한다
        deleteProcessFile(status, processFileLocation, targetJobName);
    }

    /**
     * 벨리데이터를 해석해서 JobParameters형으로 변환한다.
     *
     * @param args
     * @return
     */
    protected JobParameters getJobParameters(String[] args) {
        val props = StringUtils.splitArrayElementsIntoProperties(args, "=");

        if (log.isDebugEnabled() && props != null) {
            props.entrySet().stream()
                    .forEach(e -> log.debug("args: key={}, value={}", e.getKey(), String.valueOf(e.getValue())));
        }

        return converter.getJobParameters(props);
    }

    /**
     * 이중 기동 방지 파일을 작성한다.
     *
     * @param processFileLocation
     * @param jobName
     */
    protected void createProcessFile(String processFileLocation, String jobName) throws IOException {
        // 이중 기동 방지 파일의 보관처를 작성한다
        val location = Paths.get(processFileLocation);
        FileUtils.createDirectory(location);

        // 이중 기동 방지 파일을 작성한다
        val path = location.resolve(jobName);
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            log.error("이중 기동 파일이 존재하므로 처리를 중단합니다. [path={}]", path.toAbsolutePath().toString());
            throw e;
        }
    }

    /**
     * 이중 기동 방지 파일을 삭제한다
     *
     * @param status
     * @param processFileLocation
     * @param jobName
     */
    protected void deleteProcessFile(BatchStatus status, String processFileLocation, String jobName)
            throws IOException {

        if (status == BatchStatus.COMPLETED) {
            val location = Paths.get(processFileLocation);
            val path = location.resolve(jobName);
            Files.deleteIfExists(path);
        }
    }

    protected BatchStatus execute(Job job, JobParameters jobParameters)
            throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException, JobParametersNotFoundException {

        BatchStatus status = BatchStatus.UNKNOWN;
        val nextParameters = getNextJobParameters(job, jobParameters);

        if (nextParameters != null) {
            val execution = jobLauncher.run(job, nextParameters);

            if (publisher != null) {
                publisher.publishEvent(new JobExecutionEvent(execution));
            }

            status = execution.getStatus();
        }

        return status;
    }

    protected JobParameters getNextJobParameters(Job job, JobParameters parameters) {
        String name = job.getName();
        JobParameters mergeParameters = new JobParameters();
        val lastInstances = jobExplorer.getJobInstances(name, 0, 1);
        val incrementer = job.getJobParametersIncrementer();
        val additional = parameters.getParameters();

        if (lastInstances.isEmpty()) {
            // Start from a completely clean sheet
            if (incrementer != null) {
                mergeParameters = incrementer.getNext(new JobParameters());
            }
        } else {
            val previousExecutions = jobExplorer.getJobExecutions(lastInstances.get(0));
            val previousExecution = previousExecutions.get(0);

            if (previousExecution == null) {
                // Normally this will not happen - an instance exists with no executions
                if (incrementer != null) {
                    mergeParameters = incrementer.getNext(new JobParameters());
                }
            } else if (isStoppedOrFailed(previousExecution) && job.isRestartable()) {
                // Retry a failed or stopped execution
                mergeParameters = previousExecution.getJobParameters();
                // Non-identifying additional parameters can be removed to a retry
                removeNonIdentifying(additional);
            } else if (incrementer != null) {
                // New instance so increment the parameters if we can
                mergeParameters = incrementer.getNext(previousExecution.getJobParameters());
            }
        }

        return merge(mergeParameters, additional);
    }

    protected boolean isStoppedOrFailed(JobExecution execution) {
        val status = execution.getStatus();
        return (status == BatchStatus.STOPPED || status == BatchStatus.FAILED);
    }

    protected void removeNonIdentifying(Map<String, JobParameter> parameters) {
        Map<String, JobParameter> copy = new HashMap<>(parameters);

        for (val parameter : copy.entrySet()) {
            if (!parameter.getValue().isIdentifying()) {
                parameters.remove(parameter.getKey());
            }
        }
    }

    protected JobParameters merge(JobParameters merged, Map<String, JobParameter> parameters) {
        Map<String, JobParameter> temp = new HashMap<>();
        temp.putAll(merged.getParameters());
        temp.putAll(parameters);
        merged = new JobParameters(temp);
        return merged;
    }
}
