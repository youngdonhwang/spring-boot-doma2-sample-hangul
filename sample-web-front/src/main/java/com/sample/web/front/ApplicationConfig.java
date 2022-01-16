package com.sample.web.front;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.sample.web.base.BaseApplicationConfig;

@Configuration
@EnableCaching // JCache를 활성화한다
public class ApplicationConfig extends BaseApplicationConfig {
}
