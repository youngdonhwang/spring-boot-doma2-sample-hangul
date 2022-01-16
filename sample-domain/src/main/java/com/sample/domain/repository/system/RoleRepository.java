package com.sample.domain.repository.system;

import static com.sample.common.util.ValidateUtils.isNotEmpty;
import static com.sample.common.util.ValidateUtils.isTrue;
import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.RoleDao;
import com.sample.domain.dao.system.RolePermissionDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Role;
import com.sample.domain.dto.system.RoleCriteria;
import com.sample.domain.dto.system.RolePermission;
import com.sample.domain.dto.system.RolePermissionCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 역할 저장소
 */
@Repository
public class RoleRepository extends BaseRepository {

    @Autowired
    RoleDao roleDao;

    @Autowired
    RolePermissionDao rolePermissionDao;

    /**
     * 역할을 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<Role> findAll(RoleCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다.
        val options = createSelectOptions(pageable).count();
        val data = roleDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 권한을 취득한다.
     *
     * @param criteria
     * @return
     */
    public Optional<Role> findOne(RoleCriteria criteria) {
        // 1건 취득
        val role = roleDao.select(criteria);

        role.ifPresent(r -> {
            val rolePermissions = findRolePermissions(r);
            if (isNotEmpty(rolePermissions)) {
                Map<Integer, Boolean> permissions = new HashMap<>();
                rolePermissions.forEach(rp -> permissions.putIfAbsent(rp.getPermissionId(), true));
                r.setPermissions(permissions);
            }
        });

        return role;
    }

    /**
     * 역할을 취득한다.
     *
     * @return
     */
    public Role findById(final Long id) {
        // 1건 취득
        val role = roleDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " 의 데이터가 없습니다."));

        val rolePermissions = findRolePermissions(role);
        if (isNotEmpty(rolePermissions)) {
            Map<Integer, Boolean> permissions = new HashMap<>();
            rolePermissions.forEach(rp -> permissions.putIfAbsent(rp.getPermissionId(), true));
            role.setPermissions(permissions);
        }

        return role;
    }

    /**
     * 역할을 추가한다.
     *
     * @param inputRole
     * @return
     */
    public Role create(final Role inputRole) {
        // 1건 등록
        roleDao.insert(inputRole);

        // 역할 권한 관계를 등록한다
        insertRolePermissions(inputRole);

        return inputRole;
    }

    /**
     * 역할을 갱신한다.
     *
     * @param inputRole
     * @return
     */
    public Role update(final Role inputRole) {
        // 1건 갱신
        int updated = roleDao.update(inputRole);

        if (updated < 1) {
            throw new NoDataFoundException("role_id=" + inputRole.getId() + " 의 데이터가 없습니다.");
        }

        // 역할 권한 관계를 논리 삭제한다
        deleteRolePermissions(inputRole);

        // 역할 권한 관계를 등록한다.
        insertRolePermissions(inputRole);

        return inputRole;
    }

    /**
     * 역할을 논리 삭제한다.
     *
     * @return
     */
    public Role delete(final Long id) {
        val role = roleDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " 의 데이터가 없습니다."));

        int updated = roleDao.delete(role);

        if (updated < 1) {
            throw new NoDataFoundException("role_id=" + id + " 는 갱신할 수 없었습니다.");
        }

        // 역할 권한 관계를 논리 삭제한다
        deleteRolePermissions(role);

        return role;
    }

    /**
     * 역할 권한 관계를 등록한다.
     *
     * @param inputRole
     */
    protected void insertRolePermissions(final Role inputRole) {
        // 입력값이 없는 경우는 스킵한다
        if (isNotEmpty(inputRole.getPermissions())) {
            List<RolePermission> rolePermissionsToInsert = new ArrayList<>();

            // 권한 체크가 있는 경우
            inputRole.getPermissions().forEach((key, value) -> {
                // 체크되어 있다
                if (isTrue(value)) {
                    val rolePermission = new RolePermission();
                    rolePermission.setRoleKey(inputRole.getRoleKey());
                    rolePermission.setPermissionId(key);
                    rolePermissionsToInsert.add(rolePermission);
                }
            });

            // 일괄 등록
            rolePermissionDao.insert(rolePermissionsToInsert);
        }
    }

    /**
     * 역할 권한 관계를 논리 삭제한다
     *
     * @param inputRole
     */
    protected void deleteRolePermissions(final Role inputRole) {
        List<RolePermission> rolePermissionsToDelete = findRolePermissions(inputRole);

        if (isNotEmpty(rolePermissionsToDelete)) {
            rolePermissionDao.delete(rolePermissionsToDelete);// 일괄 논리 삭제
        }
    }

    /**
     * 역할 권한 관계를 취득한다.
     *
     * @param inputRole
     * @return
     */
    protected List<RolePermission> findRolePermissions(Role inputRole) {
        // 역할 권한 관계를 역할키로 취득한다
        val criteria = new RolePermissionCriteria();
        criteria.setRoleKey(inputRole.getRoleKey());

        val options = createSelectOptions(Pageable.NO_LIMIT);
        return rolePermissionDao.selectAll(criteria, options, toList());
    }
}
