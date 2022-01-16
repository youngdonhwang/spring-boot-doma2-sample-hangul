package com.sample.batch.listener;

import static com.sample.batch.BatchConst.MDC_BATCH_ID;
import static com.sample.domain.Const.YYYY_MM_DD_HHmmss;

import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;
import com.sample.common.util.DateUtils;
import com.sample.common.util.MDCUtils;
import com.sample.domain.dao.AuditInfoHolder;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseJobExecutionListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        val batchId = getBatchId();
        val batchName = getBatchName();
        val startTime = jobExecution.getStartTime();
        val startDateTime = DateUtils.toLocalDateTime(startTime);

        MDCUtils.putIfAbsent(MDC_BATCH_ID, batchId);

        log.info("*********************************************");
        log.info("* 배치ID : {}", batchId);
        log.info("* 배치명 : {}", batchName);
        log.info("* 시작시간 : {}", DateUtils.format(startTime, YYYY_MM_DD_HHmmss));
        log.info("*********************************************");

        // 감사 정보를 설정한다
        AuditInfoHolder.set(batchId, startDateTime);

        // 컨텍스트를 설정한다
        val context = BatchContextHolder.getContext();
        context.set(batchId, batchName, startDateTime);

        // 機能別の初期化処理を呼び出す
        before(jobExecution, context);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // 컨텍스트를 추출한다
        val context = BatchContextHolder.getContext();

        // 기능별 종료처리를 호출한다
        try {
            after(jobExecution, context);
        } catch (Throwable t) {
            log.error("exception occurred. ", t);
            throw new IllegalStateException(t);
        } finally {
            // 共通の終了処理
            try {
                val batchId = context.getBatchId();
                val batchName = context.getBatchName();
                val jobStatus = jobExecution.getStatus();
                val endTime = jobExecution.getEndTime();

                if (log.isDebugEnabled()) {
                    val jobId = jobExecution.getJobId();
                    val jobInstance = jobExecution.getJobInstance();
                    val jobInstanceId = jobInstance.getInstanceId();
                    log.debug("job executed. [job={}(JobInstanceId:{} status:{})] in {}ms", jobId, jobInstanceId,
                            jobStatus, took(jobExecution));
                    jobExecution.getStepExecutions()
                            .forEach(s -> log.debug("step executed. [step={}] in {}ms", s.getStepName(), took(s)));
                }

                if (!jobStatus.isRunning()) {
                    log.info("*********************************************");
                    log.info("* 배치ID   : {}", batchId);
                    log.info("* 배치명   : {}", batchName);
                    log.info("* 상태     : {}", jobStatus.getBatchStatus().toString());
                    log.info("* 대상건수 : {}", context.getTotalCount());
                    log.info("* 처리건수 : {}", context.getProcessCount());
                    log.info("* 오류건수 : {}", context.getErrorCount());
                    log.info("* 종료시각 : {}", DateUtils.format(endTime, YYYY_MM_DD_HHmmss));
                    log.info("*********************************************");
                }
            } finally {
                MDC.remove(MDC_BATCH_ID);

                // 감사 정보를 클리어한다
                AuditInfoHolder.clear();

                // Job 컨텍스트를 클리어한다
                context.clear();
            }
        }
    }

    protected long took(JobExecution jobExecution) {
        return jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
    }

    protected long took(StepExecution stepExecution) {
        return stepExecution.getEndTime().getTime() - stepExecution.getStartTime().getTime();
    }

    /**
     * 배치ID를 반환한다.
     *
     * @return
     */
    protected abstract String getBatchId();

    /**
     * 배치명을 반환한다.
     *
     * @return
     */
    protected abstract String getBatchName();

    /**
     * 기능별 초기화 처리를 호출한다
     * 
     * @param jobExecution
     * @param context
     */
    protected void before(JobExecution jobExecution, BatchContext context) {
    }

    /**
     * 기능별 종료처리를 호출한다
     *
     * @param jobExecution
     * @param context
     */
    protected void after(JobExecution jobExecution, BatchContext context) {
    }
}
