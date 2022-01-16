package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.RolePermission;
import com.sample.domain.dto.system.RolePermissionCriteria;

@ConfigAutowireable
@Dao
public interface RolePermissionDao {

    /**
     * 역할권한관계를 취득한다.
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final RolePermissionCriteria criteria, final SelectOptions options,
            final Collector<RolePermission, ?, R> collector);

    /**
     * 역할권한관계를 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<RolePermission> selectById(Long id);

    /**
     * 역할권한관계를 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<RolePermission> select(RolePermissionCriteria criteria);

    /**
     * 역할권한관계를 등록한다.
     *
     * @param rolePermission
     * @return
     */
    @Insert
    int insert(RolePermission rolePermission);

    /**
     * 역할권한관계를 갱신한다.
     *
     * @param rolePermission
     * @return
     */
    @Update
    int update(RolePermission rolePermission);

    /**
     * 역할권한관계를 논리삭제한다.
     *
     * @param rolePermission
     * @return
     */
    @Update(excludeNull = true) // NULL 항목은 갱신 대상으로 하지 않는다
    int delete(RolePermission rolePermission);

    /**
     * 역할권한관계를 일관 논리 삭제한다.
     *
     * @param rolePermissions
     * @return
     */
    @BatchUpdate
    int[] delete(List<RolePermission> rolePermissions);

    /**
     * 역할권한관계를 일괄 등록한다.
     *
     * @param rolePermissions
     * @return
     */
    @BatchInsert
    int[] insert(List<RolePermission> rolePermissions);
}
