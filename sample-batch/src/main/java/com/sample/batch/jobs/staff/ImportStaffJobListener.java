package com.sample.batch.jobs.staff;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.batch.core.JobExecution;

import com.sample.batch.context.BatchContext;
import com.sample.batch.listener.BaseJobExecutionListener;

import lombok.val;

public class ImportStaffJobListener extends BaseJobExecutionListener {

    @Override
    protected String getBatchId() {
        return "BATCH_001";
    }

    @Override
    protected String getBatchName() {
        return "담당자정보 데이터 로드";
    }

    @Override
    protected void before(JobExecution jobExecution, BatchContext context) {
        // 전날을 대상으로 한다
        val yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        context.getAdditionalInfo().putIfAbsent("targetDate", yesterday);
    }

    @Override
    protected void after(JobExecution jobExecution, BatchContext context) {
        // 종료하기 직전에 호출된다
    }
}
