package com.sample.domain.dao.system

import com.sample.domain.dao.system.CodeDao
import com.sample.domain.dto.system.CodeCriteria
import org.seasar.doma.jdbc.SelectOptions
import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static java.util.stream.Collectors.toList
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeDaoTest extends Specification {

    @Autowired
    CodeDao codeDao

    def "selectAll의 결과가 null이 아닐 것"() {
        expect:
        def criteria = new CodeCriteria()
        def options = SelectOptions.get().offset(0).limit(10)
        def codeList = codeDao.selectAll(criteria, options, toList())
        Assert.notNull(codeList)
    }
}
