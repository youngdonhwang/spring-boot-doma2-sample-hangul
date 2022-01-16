package com.sample.web.base.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 문자열의 입력값을 LocalDateTime형으로 변환한다
 */
public final class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final DateTimeFormatter formatter;

    /**
     * 생성자
     * 
     * @param dateFormat
     */
    public LocalDateTimeConverter(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDateTime convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        return LocalDateTime.parse(source, formatter);
    }
}
