package com.sample.web.admin.controller.html.system.codes;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 코드등록 입력체크
 */
@Component
public class CodeFormValidator extends AbstractValidator<CodeForm> {

    @Override
    protected void doValidate(CodeForm form, Errors errors) {

    }
}
