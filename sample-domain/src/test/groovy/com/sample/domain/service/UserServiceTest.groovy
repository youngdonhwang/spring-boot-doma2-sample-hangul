package com.sample.domain.service

import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.user.UserCriteria
import com.sample.domain.service.users.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
@Transactional
// 테스트 후에 롤백한다
class UserServiceTest extends Specification {

    @Autowired
    UserService userService

    def "존재하지 않는 메일주소로 검색한 경우, 0건이 반환될 것"() {
        when:
        def criteria = new UserCriteria()
        criteria.setEmail("aaaa")

        def pages = userService.findAll(criteria, Pageable.DEFAULT)

        then:
        pages.getCount() == 0
    }

    def "주소를 IS NULL로 검색한 경우, 0건이 반환될 것"() {
        when:
        def criteria = new UserCriteria()
        criteria.setOnlyNullAddress(true)

        def pages = userService.findAll(criteria, Pageable.DEFAULT)

        then:
        pages.getCount() == 0
    }
}
