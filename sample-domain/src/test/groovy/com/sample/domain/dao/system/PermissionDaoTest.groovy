package com.sample.domain.dao.system

import com.sample.domain.dao.system.PermissionDao
import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.system.Permission
import com.sample.domain.dto.system.Permission
import com.sample.domain.dto.system.PermissionCriteria
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
class PermissionDaoTest extends Specification {

    @Autowired
    PermissionDao permissionDao

    def "존재하지 않는 ID로 검색한 경우, 빈 리스트가 반환될 것"() {
        when:
        def options = createSelectOptions(Pageable.DEFAULT).count()
        def criteria = new PermissionCriteria()
        criteria.setId(-9999)

        def data = permissionDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "존재하지 않는 권한키로 검색한 경우, empty가 반환될 것"() {
        when:
        def criteria = new PermissionCriteria()
        criteria.setPermissionKey("XXXXXXXXXX")

        Optional<Permission> permission = permissionDao.select(criteria)

        then:
        permission == Optional.empty()
    }

    def "존재하지 않는 ID로 검색한 경우, empty가 반횐될 것"() {
        when:
        Optional<Permission> permission = permissionDao.selectById(-9999)

        then:
        permission == Optional.empty()
    }

    def "개정번호를 지정하지 않고 갱신한 경우, 예외가 발생할 것"() {
        when:
        def permission = new Permission()
        permission.setPermissionName("XXXXXXXXXX")
        permissionDao.update(permission)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "존재하는 데이터를 지정하여 갱신한 경우, 갱신 건수가 1건이 될 것"() {
        when:
        def permission = permissionDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("데이터가 존재하지 않습니다.") })

        permission.setPermissionName("XXXXXXXXXX")
        def updated = permissionDao.update(permission)

        then:
        updated == 1
    }
}
