package com.sample.domain.dao.system

import com.sample.domain.dao.system.RoleDao
import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.system.Role
import com.sample.domain.dto.system.Role
import com.sample.domain.dto.system.RoleCriteria
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
class RoleDaoTest extends Specification {

    @Autowired
    RoleDao roleDao

    def "존재하지 않는 ID로 검색한 결과, 빈 리스트가 반환될 것"() {
        when:
        def options = createSelectOptions(Pageable.DEFAULT).count()
        def criteria = new RoleCriteria()
        criteria.setId(-9999)

        def data = roleDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "존재하지 않는 역할키로 검색한 경우, empty가 반환될 것"() {
        when:
        def criteria = new RoleCriteria()
        criteria.setRoleKey("XXXXXXXXXX")

        Optional<Role> role = roleDao.select(criteria)

        then:
        role == Optional.empty()
    }

    def "존재하지 않은 ID로 검색한 결과, empty가 반환될 것"() {
        when:
        Optional<Role> role = roleDao.selectById(-9999)

        then:
        role == Optional.empty()
    }

    def "개정번호를 지정하지 않고 갱신한 경우, 예외가 발생할 것"() {
        when:
        def role = new Role()
        role.setRoleName("XXXXXXXXXX")
        roleDao.update(role)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "존재하는 데이터를 지정하여 갱신한 경우, 갱신 건수가 1건이 될 것"() {
        when:
        def role = roleDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("데이터가 존재하지 않습니다.") })

        role.setRoleName("XXXXXXXXXX")
        def updated = roleDao.update(role)

        then:
        updated == 1
    }
}
