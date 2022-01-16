package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;
import com.sample.domain.repository.system.PermissionRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 권한 서비스
 */
@Service
public class PermissionService extends BaseTransactionalService {

    @Autowired
    PermissionRepository permissionRepository;

    /**
     * 권한을 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용인 경우는 지정한다
    public Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return permissionRepository.findAll(criteria, pageable);
    }

    /**
     * 권한을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Permission> findOne(PermissionCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return permissionRepository.findOne(criteria);
    }

    /**
     * 권한을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Permission findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return permissionRepository.findById(id);
    }

    /**
     * 권한을 추가한다.
     *
     * @param inputPermission
     * @return
     */
    public Permission create(final Permission inputPermission) {
        Assert.notNull(inputPermission, "inputPermission must not be null");
        return permissionRepository.create(inputPermission);
    }

    /**
     * 권한을 갱신한다.
     *
     * @param inputPermission
     * @return
     */
    public Permission update(final Permission inputPermission) {
        Assert.notNull(inputPermission, "inputPermission must not be null");
        return permissionRepository.update(inputPermission);
    }

    /**
     * 권한을 논리삭제한다.
     *
     * @return
     */
    public Permission delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return permissionRepository.delete(id);
    }
}
