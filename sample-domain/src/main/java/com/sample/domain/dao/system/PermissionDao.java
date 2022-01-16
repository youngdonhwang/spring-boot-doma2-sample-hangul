package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;

@ConfigAutowireable
@Dao
public interface PermissionDao {

    /**
     * 권한을 취득한다.
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final PermissionCriteria criteria, final SelectOptions options,
            final Collector<Permission, ?, R> collector);

    /**
     * 권한을 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<Permission> selectById(Long id);

    /**
     * 권한을 1건 취득한다
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<Permission> select(PermissionCriteria criteria);

    /**
     * 권한을 등록한다.
     *
     * @param permission
     * @return
     */
    @Insert
    int insert(Permission permission);

    /**
     * 권한을 갱신한다.
     *
     * @param permission
     * @return
     */
    @Update
    int update(Permission permission);

    /**
     * 권한을 논리삭제한다.
     *
     * @param permission
     * @return
     */
    @Update(excludeNull = true) // NULL항목은 갱신 대상으로 하지 않는다
    int delete(Permission permission);

    /**
     * 권한을 일괄 등록한다.
     *
     * @param permissions
     * @return
     */
    @BatchInsert
    int[] insert(List<Permission> permissions);

    /**
     * 권한을 일괄 갱신한다.
     *
     * @param permissions
     * @return
     */
    @BatchUpdate
    int[] update(List<Permission> permissions);
}
