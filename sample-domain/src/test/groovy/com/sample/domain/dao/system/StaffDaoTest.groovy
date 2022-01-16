package com.sample.domain.dao.system

import com.sample.domain.dao.system.StaffDao
import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.system.Staff
import com.sample.domain.dto.system.Staff
import com.sample.domain.dto.system.StaffCriteria
import com.sample.domain.exception.NoDataFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static com.sample.domain.util.DomaUtils.createSelectOptions
import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
@Transactional // 테스트 후에 롤백한다
class StaffDaoTest extends Specification {

    @Autowired
    StaffDao staffDao

    def "존재하지 않는 ID로 검색한 경우, 빈 리스트를 반환할 것"() {
        when:
        def options = createSelectOptions(Pageable.DEFAULT).count()
        def criteria = new StaffCriteria()
        criteria.setId(-9999)

        def data = staffDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "존재하지 않는 메일주소로 검색한 경우, empty가 반환될 것"() {
        when:
        def criteria = new StaffCriteria()
        criteria.setEmail("XXXXXXXXXX")

        Optional<Staff> staff = staffDao.select(criteria)

        then:
        staff == Optional.empty()
    }

    def "존재하지 않는 ID로 검색한 경우, empty가 반환될 것"() {
        when:
        Optional<Staff> staff = staffDao.selectById(-9999)

        then:
        staff == Optional.empty()
    }

    def "개정번호를 지정하지 않고 갱신한 경우, 예외가 발생할 것"() {
        when:
        def staff = new Staff()
        staff.setEmail("XXXXXXXXXX")
        staffDao.update(staff)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "존재하는 데이터를 지정해서 갱신한 경우, 갱신 건수가 1건이 될 것"() {
        when:
        def staff = staffDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("데이터가 존재하지 않습니다.") })

        staff.setEmail("XXXXXXXXXX")
        def updated = staffDao.update(staff)

        then:
        updated == 1
    }
}
