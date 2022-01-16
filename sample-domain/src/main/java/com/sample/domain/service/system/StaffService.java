package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.repository.system.StaffRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 담당자 서비스
 */
@Service
public class StaffService extends BaseTransactionalService {

    @Autowired
    StaffRepository staffRepository;

    /**
     * 담당자를 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용인 경우는 지정한다
    public Page<Staff> findAll(StaffCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return staffRepository.findAll(criteria, pageable);
    }

    /**
     * 담당자를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Staff> findOne(StaffCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return staffRepository.findOne(criteria);
    }

    /**
     * 담당자를 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Staff findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return staffRepository.findById(id);
    }

    /**
     * 담당자를 추가한다.
     *
     * @param inputStaff
     * @return
     */
    public Staff create(final Staff inputStaff) {
        Assert.notNull(inputStaff, "inputStaff must not be null");
        return staffRepository.create(inputStaff);
    }

    /**
     * 담당자를 갱신한다.
     *
     * @param inputStaff
     * @return
     */
    public Staff update(final Staff inputStaff) {
        Assert.notNull(inputStaff, "inputStaff must not be null");
        return staffRepository.update(inputStaff);
    }

    /**
     * 담당자를 논리삭제한다.
     *
     * @return
     */
    public Staff delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return staffRepository.delete(id);
    }
}
