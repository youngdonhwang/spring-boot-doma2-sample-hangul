package com.sample.batch.jobs;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;

import lombok.val;

public abstract class BaseItemWriter<T> implements ItemWriter<T> {

    @SuppressWarnings("unchecked")
    @Override
    public void write(List<? extends T> items) throws Exception {
        // 컨텍스트를 추출한다
        val context = BatchContextHolder.getContext();

        // 기록한다
        doWrite(context, (List<T>) items);
    }

    /**
     * 인수로 전달된 아이템을 기록한다,
     *
     * @param context
     * @param items
     */
    protected abstract void doWrite(BatchContext context, List<T> items);
}
