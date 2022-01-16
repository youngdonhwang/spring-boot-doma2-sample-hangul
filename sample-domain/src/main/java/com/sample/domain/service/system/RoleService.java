package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Role;
import com.sample.domain.dto.system.RoleCriteria;
import com.sample.domain.repository.system.RoleRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 역할 서비스
 */
@Service
public class RoleService extends BaseTransactionalService {

    @Autowired
    RoleRepository roleRepository;

    /**
     * 역할을 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용인 경우는 지정한다
    public Page<Role> findAll(RoleCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return roleRepository.findAll(criteria, pageable);
    }

    /**
     * 역할을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Role> findOne(RoleCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return roleRepository.findOne(criteria);
    }

    /**
     * 역할을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Role findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return roleRepository.findById(id);
    }

    /**
     * 역할을 추가한다.
     *
     * @param inputRole
     * @return
     */
    public Role create(final Role inputRole) {
        Assert.notNull(inputRole, "inputRole must not be null");
        return roleRepository.create(inputRole);
    }

    /**
     * 역할을 갱신한다.
     *
     * @param inputRole
     * @return
     */
    public Role update(final Role inputRole) {
        Assert.notNull(inputRole, "inputRole must not be null");
        return roleRepository.update(inputRole);
    }

    /**
     * 역할을 논리 삭제한다.
     *
     * @return
     */
    public Role delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return roleRepository.delete(id);
    }
}
