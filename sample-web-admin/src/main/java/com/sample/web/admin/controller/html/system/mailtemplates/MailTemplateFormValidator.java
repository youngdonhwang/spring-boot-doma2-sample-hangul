package com.sample.web.admin.controller.html.system.mailtemplates;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 메일템플릿등록 입력체크
 */
@Component
public class MailTemplateFormValidator extends AbstractValidator<MailTemplateForm> {

    @Override
    protected void doValidate(MailTemplateForm form, Errors errors) {

    }
}
