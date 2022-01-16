package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Holiday;
import com.sample.domain.dto.system.HolidayCriteria;
import com.sample.domain.repository.system.HolidayRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 공휴일 서비스
 */
@Service
public class HolidayService extends BaseTransactionalService {

    @Autowired
    HolidayRepository holidayRepository;

    /**
     * 공휴일을 복수 지정한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 읽기 전용인 경우는 지정한다
    public Page<Holiday> findAll(HolidayCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return holidayRepository.findAll(criteria, pageable);
    }

    /**
     * 공휴일을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Holiday> findOne(HolidayCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return holidayRepository.findOne(criteria);
    }

    /**
     * 공휴일을 취득한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Holiday findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return holidayRepository.findById(id);
    }

    /**
     * 공휴일을 추가한다.
     *
     * @param inputHoliday
     * @return
     */
    public Holiday create(final Holiday inputHoliday) {
        Assert.notNull(inputHoliday, "inputHoliday must not be null");
        return holidayRepository.create(inputHoliday);
    }

    /**
     * 공휴일을 갱신한다.
     *
     * @param inputHoliday
     * @return
     */
    public Holiday update(final Holiday inputHoliday) {
        Assert.notNull(inputHoliday, "inputHoliday must not be null");
        return holidayRepository.update(inputHoliday);
    }

    /**
     * 공휴일을 논리 삭제한다.
     *
     * @return
     */
    public Holiday delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return holidayRepository.delete(id);
    }
}
