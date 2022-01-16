package com.sample.domain.helper

import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class CodeHelperTest extends Specification {

    @Autowired
    CodeHelper codeHelper

    def "코드분류목록을 취득할 수 있을 것"() {
        expect:
        def codeCategoryList= codeHelper.getCodeCategories()
        Assert.notNull(codeCategoryList)
    }

    def "코드분류와 코드키를 지정하여 코드값을 취득할 수 있을 것"() {
        expect:
        def code = codeHelper.getCode("GNR0001", "01")
        Assert.that(code.getCodeValue() == "남")
    }
}
