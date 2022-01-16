package com.sample.domain.dao.users

import com.sample.domain.dao.users.UserDao
import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.user.User
import com.sample.domain.dto.user.UserCriteria
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
class UserDaoTest extends Specification {

    @Autowired
    UserDao userDao

    def "존재하지 않는 ID로 검색한 경우, 빈 리스트가 반환될 것"() {
        when:
        def options = createSelectOptions(Pageable.DEFAULT).count()
        def criteria = new UserCriteria()
        criteria.setId(-9999)

        def data = userDao.selectAll(criteria, options, toList())

        then:
        data.size() == 0
    }

    def "존재하지 않는 메일 주소로 검색한 경우, empty가 반환될 것"() {
        when:
        def criteria = new UserCriteria()
        criteria.setEmail("aaaa")

        Optional<User> user = userDao.select(criteria)

        then:
        user == Optional.empty()
    }

    def "존재하지 않는 ID로 검색한 경우, empty를 반환할 것"() {
        when:
        Optional<User> user = userDao.selectById(-9999)

        then:
        user == Optional.empty()
    }

    def "개정번호를 지정하지 않고 갱신한 경우, 예외가 발생될 것"() {
        when:
        def user = new User()
        user.setEmail("test@sample.com")
        userDao.update(user)

        then:
        thrown(OptimisticLockingFailureException)
    }

    def "존재하는 메일 주소를 지정하여 갱신한 경우, 갱신 건수가 1건이 될 것"() {
        when:
        def user = userDao.selectById(1)
                .orElseThrow({ new NoDataFoundException("데이터가 존재하지 않습니다.") })

        user.setAddress("test")
        def updated = userDao.update(user)

        then:
        updated == 1
    }
}
