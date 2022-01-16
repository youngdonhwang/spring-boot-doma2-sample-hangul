package com.sample.batch.jobs;

import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 베이스 프로세서
 * 
 * @param <I>
 * @param <O>
 */
@Slf4j
public abstract class BaseItemProcessor<I, O> implements ItemProcessor<I, O> {

    @Override
    public O process(I item) {
        val validator = getValidator();
        val context = BatchContextHolder.getContext();

        // 대상 건수를 가산한다
        context.increaseTotalCount();

        if (validator != null) {
            val binder = new DataBinder(item);
            binder.setValidator(validator);
            binder.validate();

            val result = binder.getBindingResult();
            if (result.hasErrors()) {
                // 벨리데이션 오류가 있는 경우
                onValidationError(context, result, item);

                // 오류 건수를 카운트한다
                increaseErrorCount(context, result, item);

                // null을 반환하면, ItemWriter에 전달하지 않는다.
                return null;
            }
        }

        // 실제 처리
        O output = doProcess(context, item);

        // 처리 건수를 카운트한다
        increaseProcessCount(context, item);

        return output;
    }

    /**
     * エラー件数を加算します。
     * 
     * @param context
     * @param result
     * @param item
     */
    protected void increaseErrorCount(BatchContext context, BindingResult result, I item) {
        context.increaseErrorCount();
    }

    /**
     * 処理件数を加算します。
     *
     * @param context
     * @param item
     */
    protected void increaseProcessCount(BatchContext context, I item) {
        context.increaseProcessCount();
    }

    /**
     * 対象件数を加算します。
     *
     * @param context
     * @param item
     */
    protected void increaseTotalCount(BatchContext context, I item) {
        context.increaseProcessCount();
    }

    /**
     * バリデーションエラーが発生した場合に処理します。
     * 
     * @param context
     * @param result
     * @param item
     */
    protected abstract void onValidationError(BatchContext context, BindingResult result, I item);

    /**
     * 実処理を実施します。
     * 
     * @param context
     * @param item
     * @return
     */
    protected abstract O doProcess(BatchContext context, I item);

    /**
     * バリデーターを取得します。
     * 
     * @return
     */
    protected abstract Validator getValidator();

    /**
     * 例外発生時のデフォルト実装
     * 
     * @param item
     * @param e
     */
    @OnProcessError
    protected void onProcessError(I item, Exception e) {
        log.error("failed to process item.", e);
        throw new IllegalStateException(e);
    }
}
