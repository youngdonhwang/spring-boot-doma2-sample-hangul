package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Holiday;
import com.sample.domain.dto.system.HolidayCriteria;

@ConfigAutowireable
@Dao
public interface HolidayDao {

    /**
     * 공휴일을 취득한다.
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final HolidayCriteria criteria, final SelectOptions options,
            final Collector<Holiday, ?, R> collector);

    /**
     * 공휴일을 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<Holiday> selectById(Long id);

    /**
     * 공휴일을 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<Holiday> select(HolidayCriteria criteria);

    /**
     * 공휴일을 등록한다.
     *
     * @param holiday
     * @return
     */
    @Insert
    int insert(Holiday holiday);

    /**
     * 공휴일을 갱신한다.
     *
     * @param holiday
     * @return
     */
    @Update
    int update(Holiday holiday);

    /**
     * 공휴일을 논리삭제한다.
     *
     * @param holiday
     * @return
     */
    @Update(excludeNull = true) // NULL항목은 갱신대상으로 하지 않는다
    int delete(Holiday holiday);

    /**
     * 공휴일을 일괄 등록한다.
     *
     * @param holidays
     * @return
     */
    @BatchInsert
    int[] insert(List<Holiday> holidays);

    /**
     * 공휴일을 일괄 갱신한다.
     *
     * @param holidays
     * @return
     */
    @BatchUpdate
    int[] update(List<Holiday> holidays);
}
