package com.sample.web.base.security;

import static com.sample.web.base.WebConst.API_BASE_URL;

import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * 베이스 API 시큐리티 환경
 */
public abstract class BaseApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher(API_BASE_URL)
                // 모든 요청에 인증을 설정한다
                .authorizeRequests().anyRequest().authenticated()
                // Basic인증을 설정한다
                .and().httpBasic().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                // CSRF체크를 하지 않는다
                .and().csrf().disable();
    }
}
