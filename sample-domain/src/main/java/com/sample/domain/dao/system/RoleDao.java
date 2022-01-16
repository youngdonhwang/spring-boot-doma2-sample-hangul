package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Role;
import com.sample.domain.dto.system.RoleCriteria;

@ConfigAutowireable
@Dao
public interface RoleDao {

    /**
     * 역할을 취득한다.
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final RoleCriteria criteria, final SelectOptions options, final Collector<Role, ?, R> collector);

    /**
     * 역할을 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<Role> selectById(Long id);

    /**
     * 역할을 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<Role> select(RoleCriteria criteria);

    /**
     * 역할을 등록한다.
     *
     * @param role
     * @return
     */
    @Insert
    int insert(Role role);

    /**
     * 역할을 갱신한다.
     *
     * @param role
     * @return
     */
    @Update
    int update(Role role);

    /**
     * 역할을 논리삭제한다
     *
     * @param role
     * @return
     */
    @Update(excludeNull = true) // NULL 항목은 갱신 대상으로 하지 않는다.
    int delete(Role role);

    /**
     * 역할을 일괄 등록한다.
     *
     * @param roles
     * @return
     */
    @BatchInsert
    int[] insert(List<Role> roles);

    /**
     * 역할을 일괄 갱신한다.
     *
     * @param roles
     * @return
     */
    @BatchUpdate
    int[] update(List<Role> roles);
}
