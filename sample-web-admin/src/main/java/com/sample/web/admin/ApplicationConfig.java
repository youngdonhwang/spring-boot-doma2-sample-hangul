package com.sample.web.admin;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.web.base.BaseApplicationConfig;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableCaching //  JCache를 활성화한다
@EnableSwagger2 // Swagger를 활성화한다
public class ApplicationConfig extends BaseApplicationConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .select()
                // HTTP 메소드에서의 필터링도 가능
                .apis(RequestHandlerSelectors.withMethodAnnotation(GetMapping.class))
                // api를 포함하는 경로를 API그룹으로 설정
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(apiinfo());
    }

    @Bean
    public Docket admin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .select()
                .apis(RequestHandlerSelectors.any()) // all
                .paths(PathSelectors.any()) // all
                .build()
                .apiInfo(admininfo());
    }

    private ApiInfo apiinfo() {
        return new ApiInfoBuilder()
                .title("REST API List")
                .description("REST API 의 목록입니다.")
                .version("1.0")
                .contact(new Contact("takeshi.hirosue","http://www.bigtreetc.com/","takeshi.hirosue@bigtreetc.com"))
                .build();
    }

    private ApiInfo admininfo() {
        return new ApiInfoBuilder()
                .title("ALL API List")
                .description("전체 API의 목록입니다.")
                .version("1.0")
                .contact(new Contact("takeshi.hirosue","http://www.bigtreetc.com/","takeshi.hirosue@bigtreetc.com"))
                .build();
    }

}
