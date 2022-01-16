package com.sample.batch.jobs;

import static com.sample.batch.BatchConst.MDC_BATCH_ID;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import com.sample.batch.context.BatchContextHolder;
import com.sample.common.util.MDCUtils;
import com.sample.domain.dao.AuditInfoHolder;

import lombok.val;

/**
 * 페이징에 대응한 ItemReader의 베이스 클래스
 * 
 * @param <T>
 */
public abstract class BasePageableItemReader<T> extends AbstractPagingItemReader<T> {

    @Override
    protected T doRead() throws Exception {
        val context = BatchContextHolder.getContext();
        val batchId = context.getBatchId();

        // ThreadPool을 사용하고 있는 경우는 재설정한다
        MDCUtils.putIfAbsent(MDC_BATCH_ID, batchId);

        val startDateTime = context.getStartDateTime();
        AuditInfoHolder.set(batchId, startDateTime);

        return super.doRead();
    }

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }

        results.addAll(getList());
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }

    /**
     * 검색 옵션을 반환한다
     * 
     * @return
     */
    protected SelectOptions getSelectOptions() {
        val page = getPage(); // 1페이지는 0이 된다
        val perpage = getPageSize();
        val offset = page * perpage;
        return SelectOptions.get().offset(offset).limit(perpage);
    }

    /**
     * 페이징 처리한 리스트를 반환한다.
     * 
     * @return
     */
    protected abstract List<T> getList();
}
