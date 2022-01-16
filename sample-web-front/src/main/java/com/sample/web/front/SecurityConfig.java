package com.sample.web.front;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.sample.web.base.security.BaseSecurityConfig;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 어노테이션으로 롤
@Configuration
public class SecurityConfig extends BaseSecurityConfig {

}
