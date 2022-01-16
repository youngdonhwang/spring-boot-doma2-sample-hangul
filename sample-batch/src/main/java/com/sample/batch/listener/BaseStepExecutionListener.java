package com.sample.batch.listener;

import static com.sample.batch.BatchConst.MDC_BATCH_ID;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;
import com.sample.common.util.MDCUtils;
import com.sample.domain.dao.AuditInfoHolder;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseStepExecutionListener extends StepExecutionListenerSupport {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        val context = BatchContextHolder.getContext();

        // MDC를 설정한다
        setMDCIfEmpty(context, stepExecution);

        // 감사정보를 설정한다
        setAuditInfoIfEmpty(context);

        // 기능별 초기화 처리를 호출한다
        before(context, stepExecution);

        // 로그 출력
        logBeforeStep(context, stepExecution);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        val context = BatchContextHolder.getContext();

        // 기능별 종료 처리를 호출한다
        try {
            after(context, stepExecution);
        } catch (Exception e) {
            log.error("exception occurred. ", e);
            throw new IllegalStateException(e);
        }

        // 로그 출력
        logAfterStep(context, stepExecution);
        ExitStatus exitStatus = stepExecution.getExitStatus();
        return exitStatus;
    }

    /**
     * MDC의 ThreadLocal변수가 추출되지 않을 때는 필요에 따라 설정을 바꾼다.
     *
     * @param context
     * @param stepExecution
     */
    protected void setMDCIfEmpty(BatchContext context, StepExecution stepExecution) {
        val batchId = context.getBatchId();
        MDCUtils.putIfAbsent(MDC_BATCH_ID, batchId);
    }

    /**
     * 스레드 풀을 사용하고 있으면 ThreadLocal로부터 값을 추출하지 못하는 경우가 있으므로 설정을 바꾼다.
     *
     * @param context
     */
    protected void setAuditInfoIfEmpty(BatchContext context) {
        val batchId = context.getBatchId();
        val startDateTime = context.getStartDateTime();

        AuditInfoHolder.set(batchId, startDateTime);
    }

    /**
     * 스텝 시작시에 로그를 출력한다.
     *
     * @param context
     * @param stepExecution
     */
    protected void logBeforeStep(BatchContext context, StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        log.info("Step:{} ---- START ----", stepName);
    }

    /**
     * 스텝 종료 시에 로그를 출력한다.
     * 
     * @param context
     * @param stepExecution
     */
    protected void logAfterStep(BatchContext context, StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        log.info("Step:{} ---- END ----", stepName);
    }

    /**
     * 기능별 초기화 처리를 호출한다.
     * 
     * @param context
     * @param stepExecution
     */
    protected void before(BatchContext context, StepExecution stepExecution) {
    }

    /**
     * 기능별 종료 처리를 호춢한다.
     * 
     * @param context
     * @param stepExecution
     */
    protected void after(BatchContext context, StepExecution stepExecution) {
    }
}
