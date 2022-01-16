package com.sample.web.base.controller.api.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResourceImpl extends ResourceImpl {

    // 요청ID
    String requestId;

    // 입력 오류
    List<FieldErrorResource> fieldErrors;

    public ErrorResourceImpl() {
    }
}
