package com.sample.web.base.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 문자열의 입력값을 LocalDate형으로 변환한다
 */
public class LocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    /**
     * 생성자
     * 
     * @param dateFormat
     */
    public LocalDateConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDate convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        return LocalDate.parse(source, formatter);
    }
}
