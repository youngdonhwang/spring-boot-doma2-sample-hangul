package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.dto.system.StaffRole;

@ConfigAutowireable
@Dao
public interface StaffRoleDao {

    /**
     * 담당자권한을 취득한다.
     *
     * @param staffCriteria
     * @param permissionCriteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final StaffCriteria staffCriteria, final PermissionCriteria permissionCriteria,
            final SelectOptions options, final Collector<StaffRole, ?, R> collector);

    /**
     * 담당자권한을 취득한다.
     * 
     * @param id
     * @param collector
     * @param <R>
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectByStaffId(Long id, final Collector<StaffRole, ?, R> collector);

    /**
     * 담당자권한을 1건 취득한다.
     *
     * @param id
     * @return
     */
    @Select
    Optional<Permission> selectById(Long id);

    /**
     * 담당자권한을 1건 취득한다.
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<Permission> select(PermissionCriteria criteria);

    /**
     * 담당자권한을 등록한다.
     *
     * @param staffRole
     * @return
     */
    @Insert(exclude = { "roleName", "permissionKey", "permissionName" })
    int insert(StaffRole staffRole);

    /**
     * 담당자권한을 갱신한다.
     *
     * @param staffRole
     * @return
     */
    @Update(exclude = { "roleName", "permissionKey", "permissionName" })
    int update(StaffRole staffRole);

    /**
     * 담당자권한을 논리 삭제한다.
     *
     * @param staffRole
     * @return
     */
    @Update(excludeNull = true) // NULL항목은 갱신 대상으로 하지 않는다
    int delete(StaffRole staffRole);

    /**
     * 담당자권한을 일괄 등록한다.
     *
     * @param staffRoles
     * @return
     */
    @BatchInsert(exclude = { "roleName", "permissionKey", "permissionName" })
    int[] insert(List<StaffRole> staffRoles);
}
