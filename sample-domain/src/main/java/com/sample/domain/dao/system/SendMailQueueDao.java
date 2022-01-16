package com.sample.domain.dao.system;

import java.util.List;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.SendMailQueue;
import com.sample.domain.dto.system.SendMailQueueCriteria;

@ConfigAutowireable
@Dao
public interface SendMailQueueDao {

    /**
     * 메일 송신 큐를 취득한다.
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final SendMailQueueCriteria criteria, final SelectOptions options,
            final Collector<SendMailQueue, ?, R> collector);

    /**
     * 메일 송신 큐를 일괄 등록한다.
     *
     * @param sendMailQueues
     * @return
     */
    @BatchInsert
    int[] insert(List<SendMailQueue> sendMailQueues);

    /**
     * 메일 송신 큐를 일괄 갱신한다.
     *
     * @param sendMailQueues
     * @return
     */
    @BatchUpdate
    int[] update(List<SendMailQueue> sendMailQueues);
}
