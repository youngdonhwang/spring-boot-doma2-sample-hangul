package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;

@ConfigAutowireable
@Dao
public interface StaffDao {

    /**
     * 담당자를 취득한다.
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final StaffCriteria criteria, final SelectOptions options, final Collector<Staff, ?, R> collector);

    /**
     * 담당자를 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<Staff> selectById(Long id);

    /**
     * 담당자를 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<Staff> select(StaffCriteria criteria);

    /**
     * 담당자를 등록한다.
     *
     * @param Staff
     * @return
     */
    @Insert
    int insert(Staff Staff);

    /**
     * 담당자를 갱신한다.
     *
     * @param staff
     * @return
     */
    @Update
    int update(Staff staff);

    /**
     * 담당자를 논리삭제한다.
     *
     * @param staff
     * @return
     */
    @Update(excludeNull = true) // NULL항목은 갱신대상으로 하지 않는다
    int delete(Staff staff);

    /**
     * 담당자를 일괄 등록한다.
     *
     * @param staffs
     * @return
     */
    @BatchInsert
    int[] insert(List<Staff> staffs);
}
