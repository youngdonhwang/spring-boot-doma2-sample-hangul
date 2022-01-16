package com.sample.web.base.controller.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResource {

    // 항목명
    String fieldName;

    // 오류 타입
    String errorType;

    // 오류 메시지
    String errorMessage;
}
