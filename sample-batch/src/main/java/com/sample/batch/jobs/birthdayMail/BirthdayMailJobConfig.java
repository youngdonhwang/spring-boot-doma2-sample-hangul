package com.sample.batch.jobs.birthdayMail;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import com.sample.batch.listener.DefaultStepExecutionListener;
import com.sample.domain.dto.system.SendMailQueue;
import com.sample.domain.dto.user.User;

import lombok.val;

/**
 * 생일축하 메일 송신 배치
 */
@Configuration
@EnableBatchProcessing
public class BirthdayMailJobConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    TaskExecutor taskExecutor;

    @Bean
    public ItemReader<User> birthdayMailUserItemReader() {
        val reader = new BirthdayMailUserItemReader();
        reader.setPageSize(100); // 100건씩 취득하는 샘플
        return reader;
    }

    @Bean
    public ItemProcessor<User, SendMailQueue> birthdayMailProcessor() {
        // User를 SendMailQueue로 변환한다
        return new BirthdayMailProcessor();
    }

    @Bean
    public ItemWriter<SendMailQueue> birthdayMailItemWriter() {
        return new BirthdayMailItemWriter();
    }

    @Bean
    public JobExecutionListener birthdayMailJobListener() {
        return new BirthdayMailJobListener();
    }

    @Bean
    public Job birthdayMailJob() {
        return jobBuilderFactory.get("birthdayMailJob").incrementer(new RunIdIncrementer())
                .listener(birthdayMailJobListener()).flow(birthdayMailStep()).end().build();
    }

    @Bean
    public Step birthdayMailStep() {
        return stepBuilderFactory.get("birthdayMailStep").listener(new DefaultStepExecutionListener())
                .<User, SendMailQueue>chunk(100).reader(birthdayMailUserItemReader()).processor(birthdayMailProcessor())
                .writer(birthdayMailItemWriter()).taskExecutor(taskExecutor)
                // 2개의 스레드에서 처리하는 샘플
                .throttleLimit(2).build();

    }
}
