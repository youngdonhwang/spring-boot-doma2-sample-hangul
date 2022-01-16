package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.HolidayDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Holiday;
import com.sample.domain.dto.system.HolidayCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 공휴일 저장소
 */
@Repository
public class HolidayRepository extends BaseRepository {

    @Autowired
    HolidayDao holidayDao;

    /**
     * 공휴일을 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<Holiday> findAll(HolidayCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다.
        val options = createSelectOptions(pageable).count();
        val data = holidayDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 공휴일을 취득한다.
     *
     * @param criteria
     * @return
     */
    public Optional<Holiday> findOne(HolidayCriteria criteria) {
        // 1건 취득
        return holidayDao.select(criteria);
    }

    /**
     * 공휴일을 취득한다.
     *
     * @return
     */
    public Holiday findById(final Long id) {
        // 1건 취득
        return holidayDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("holiday_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * 공휴일을 추가한다.
     *
     * @param inputHoliday
     * @return
     */
    public Holiday create(final Holiday inputHoliday) {
        // 1件登録
        holidayDao.insert(inputHoliday);

        return inputHoliday;
    }

    /**
     * 공휴일을 갱신한다.
     *
     * @param inputHoliday
     * @return
     */
    public Holiday update(final Holiday inputHoliday) {
        // 1건 갱신
        int updated = holidayDao.update(inputHoliday);

        if (updated < 1) {
            throw new NoDataFoundException("holiday_id=" + inputHoliday.getId() + " 의 데이터가 없습니다.");
        }

        return inputHoliday;
    }

    /**
     * 공휴일을 논리 삭제한다.
     *
     * @return
     */
    public Holiday delete(final Long id) {
        val holiday = holidayDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("holiday_id=" + id + " のデータが見つかりません。"));

        int updated = holidayDao.delete(holiday);

        if (updated < 1) {
            throw new NoDataFoundException("holiday_id=" + id + " 는 갱신할 수 없었습니다.");
        }

        return holiday;
    }
}
