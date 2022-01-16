package com.sample.web.base.controller;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.sample.common.util.MessageUtils;

public class BaseController {

    public static final String VALIDATION_ERROR = "ValidationError";

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected ModelMapper modelMapper;

    /**
     * 입력 오류의 공통 메시지를 반환한다.
     * 
     * @return
     */
    protected String getValidationErrorMessage() {
        return getMessage(VALIDATION_ERROR);
    }

    /**
     * 컨텍스트를 반환한다.
     * 
     * @return
     */
    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 메시지를 취득한다.
     *
     * @param key
     * @param args
     * @return
     */
    protected String getMessage(String key, Object... args) {
        return MessageUtils.getMessage(key, args);
    }

    /**
     * 로케일을 지정하여 메시지를 취득한다.
     *
     * @param key
     * @param args
     * @param locale
     * @return
     */
    protected String getMessage(String key, Object[] args, Locale locale) {
        return MessageUtils.getMessage(key, args, locale);
    }
}
