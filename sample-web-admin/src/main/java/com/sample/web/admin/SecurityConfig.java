package com.sample.web.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.sample.web.base.security.BaseSecurityConfig;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 어노테이션으로 역할, 권한 체크를 실시하기 위해 정의한다
@Configuration
public class SecurityConfig extends BaseSecurityConfig {

}
