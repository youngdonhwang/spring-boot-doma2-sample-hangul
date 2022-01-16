package com.sample.batch.jobs;

import static com.sample.batch.BatchConst.EXECUTION_STATUS_SKIP;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;

import lombok.val;

/**
 * 베이스 잡 디사이더
 */
public abstract class BaseJobDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        val context = jobExecution.getExecutionContext();

        if (!decideToProceed(context)) {
            return new FlowExecutionStatus(EXECUTION_STATUS_SKIP);
        }

        return FlowExecutionStatus.COMPLETED;
    }

    /**
     * False를 반환한 경우는 처리를 스킵한다.
     * 
     * @param context
     * @return
     */
    protected abstract boolean decideToProceed(ExecutionContext context);
}
