package com.sample.web.admin.controller.html.home

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import spock.lang.Specification

@SpringBootTest
class MakeAPIDocTest extends Specification {

    @Autowired
    WebApplicationContext wac

    @Shared
    MockMvc mvc

    def setup() {
        mvc = webAppContextSetup(wac).build()
    }


    def "테스트에서API문서를생성"() {
        expect:
        mvc.perform(
                get("/v2/api-docs?group=api").accept(MediaType.APPLICATION_JSON))  // REST-API의 문서 생성
                .andDo(Swagger2MarkupResultHandler
                        .outputDirectory("build/asciidoc/snippets").build())  // 출력 디렉터리 설정
                .andExpect(status().isOk())
    }

}
