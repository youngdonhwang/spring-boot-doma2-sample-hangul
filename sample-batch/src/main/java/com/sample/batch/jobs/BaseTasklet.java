package com.sample.batch.jobs;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;
import com.sample.batch.item.ItemPosition;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 基底タスクレット
 */
@Slf4j
public abstract class BaseTasklet<I extends ItemPosition> implements Tasklet {

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    @Qualifier("beanValidator")
    protected Validator beanValidator;

    /**
     * 메인 메소드
     * 
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        val itemValidator = getValidator();
        val context = BatchContextHolder.getContext();

        val springValidator = new SpringValidator<I>();
        springValidator.setValidator(itemValidator);

        // 처리 대상을 읽어들인다.
        val streams = doRead(context);

        for (int i = 0; i < streams.size(); i++) {
            try (val stream = streams.get(i)) {
                int[] idx = { 1 };

                stream.forEach(item -> {
                    // 처음・끝의 플래그를 On한다
                    setItemPosition(item, idx[0]);

                    // 대상 건수를 가산한다
                    increaseTotalCount(context, item);

                    val binder = new DataBinder(item);
                    binder.addValidators(beanValidator, itemValidator);
                    binder.validate();

                    val br = binder.getBindingResult();
                    if (br.hasErrors()) {
                        // 오류 건수를 카운트한다
                        increaseErrorCount(context, br, item);

                        // 벨리데이션 오류가 있는 경우
                        onValidationError(context, br, item);
                    }

                    if (!br.hasErrors()) {
                        // 실제 처리
                        doProcess(context, item);

                        // 처리 건수를 카운트한다
                        increaseProcessCount(context, item);
                    }

                    idx[0]++;
                });
            }
        }

        return RepeatStatus.FINISHED;
    }

    /**
     * 처음・끝의 플래그를 설정한다.
     *
     * @param itemPosition
     * @param i
     */
    protected void setItemPosition(ItemPosition itemPosition, int i) {
        // 행수를 설정한다
        itemPosition.setPosition(i);
    }

    /**
     * 오류 건수를 가산한다.
     * 
     * @param context
     * @param br
     * @param item
     */
    protected void increaseErrorCount(BatchContext context, BindingResult br, I item) {
        context.increaseErrorCount();
    }

    /**
     * 처리 건수를 가산한다.
     * 
     * @param context
     * @param item
     */
    protected void increaseProcessCount(BatchContext context, I item) {
        context.increaseProcessCount();
    }

    /**
     * 대상 건수를 가산한다.
     *
     * @param context
     * @param item
     */
    protected void increaseTotalCount(BatchContext context, I item) {
        context.increaseTotalCount();
    }

    /**
     * 벨리데이션 오류가 발생한 경우에 처리한다.
     * 
     * @param context
     * @param br
     * @param item
     */
    protected abstract void onValidationError(BatchContext context, BindingResult br, I item);

    /**
     * 처리 대상을 읽어들인다.
     *
     * @param context
     * @return
     */
    protected abstract List<Stream<I>> doRead(BatchContext context) throws IOException;

    /**
     * 실제 처리를 실시한다.
     *
     * @param context
     * @param item
     * @return
     */
    protected abstract void doProcess(BatchContext context, I item);

    /**
     * 벨리데이터를 취득한다.
     *
     * @return
     */
    protected abstract Validator getValidator();

    /**
     * 예외 발생시의 디폴트 구현
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
