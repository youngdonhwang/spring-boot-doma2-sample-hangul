package com.sample.web.admin.controller.html.system.roles;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 역할등록 입력체크
 */
@Component
public class RoleFormValidator extends AbstractValidator<RoleForm> {

    @Override
    protected void doValidate(RoleForm form, Errors errors) {

    }
}
