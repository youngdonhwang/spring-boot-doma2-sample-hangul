package com.sample.domain.service

import com.sample.domain.dto.common.Pageable
import com.sample.domain.dto.system.CodeCriteria
import com.sample.domain.service.system.CodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeServiceTest extends Specification {

    @Autowired
    CodeService codeService

    def "코드목록을 취득할 수 있을 것"() {
        when:
        def criteria = new CodeCriteria()
        def pages = codeService.findAll(criteria, Pageable.NO_LIMIT)

        then:
        pages.getCount() >= 1
    }
}
