package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.StaffDao;
import com.sample.domain.dao.system.StaffRoleDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.dto.system.StaffRole;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 담당자 저장소
 */
@Repository
public class StaffRepository extends BaseRepository {

    @Autowired
    StaffDao staffDao;

    @Autowired
    StaffRoleDao staffRoleDao;

    /**
     * 담당자를 복수 취득한다.
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<Staff> findAll(StaffCriteria criteria, Pageable pageable) {
        // 페이징을 지정한다
        val options = createSelectOptions(pageable).count();
        val data = staffDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 담당자를 취득한다.
     *
     * @param criteria
     * @return
     */
    public Optional<Staff> findOne(StaffCriteria criteria) {
        return staffDao.select(criteria);
    }

    /**
     * 담당자를 취득한다.
     *
     * @return
     */
    public Staff findById(final Long id) {
        return staffDao.selectById(id).orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * 담당자를 추가한다.
     *
     * @param inputStaff
     * @return
     */
    public Staff create(final Staff inputStaff) {
        // 1건 등록
        staffDao.insert(inputStaff);

        // 역할 권한 관계를 등록한다
        val staffRole = new StaffRole();
        staffRole.setStaffId(inputStaff.getId());
        staffRole.setRoleKey("admin");
        staffRoleDao.insert(staffRole);

        return inputStaff;
    }

    /**
     * 담당자를 갱신한다.
     *
     * @param inputStaff
     * @return
     */
    public Staff update(final Staff inputStaff) {
        // 1건 갱신
        int updated = staffDao.update(inputStaff);

        if (updated < 1) {
            throw new NoDataFoundException("staff_id=" + inputStaff.getId() + " 의 데이터가 발견되지 않았습니다.");
        }

        return inputStaff;
    }

    /**
     * 담당자를 논리 삭제한다.
     *
     * @return
     */
    public Staff delete(final Long id) {
        val staff = staffDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " 의 데이터가 없습니다."));

        int updated = staffDao.delete(staff);

        if (updated < 1) {
            throw new NoDataFoundException("staff_id=" + id + "는 갱신할 수 없었습니다.");
        }

        return staff;
    }
}
