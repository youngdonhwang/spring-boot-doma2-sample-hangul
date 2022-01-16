package com.sample.web.base;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.sample.web.base.security.DoubleSubmitCheckingRequestDataValueProcessor;

/**
 * CsrfRequestDataValueProcessor와 자기가 직접 만든 RequestDataValueProcessor를 공존시키기 위한 설정<br/>
 * META-INF/spring.factories에 이 클래스명을 기술한다
 */
@Configuration
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class RequestDataValueProcessorAutoConfiguration {

    // requestDataValueProcessor라는 명칭이여야 한다
    @Bean
    public RequestDataValueProcessor requestDataValueProcessor() {
        // 이중 송신 방지를 위한 토큰을 자동으로 심어넣는다
        return new DoubleSubmitCheckingRequestDataValueProcessor();
    }
}
