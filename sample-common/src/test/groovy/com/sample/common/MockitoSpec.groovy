package com.sample.common

import spock.lang.Specification

class MockitoSpec extends Specification {

    def "currentTimeMillis의 반환값이 100L일 것"() {
        setup:
        def mockedSystem = GroovyMock(System)
        mockedSystem.currentTimeMillis() >> 100

        expect:
        mockedSystem.currentTimeMillis() == 100
    }
}
