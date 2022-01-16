package com.sample.web.admin.controller.html.login;

import static com.sample.common.util.ValidateUtils.isNotEquals;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 코드 등록 입력 체크
 */
@Component
public class ChangePasswordFormValidator extends AbstractValidator<ChangePasswordForm> {

    @Override
    protected void doValidate(ChangePasswordForm form, Errors errors) {
        // 확인용 패스워드와 비교해본다
        if (isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
            errors.rejectValue("password", "changePassword.unmatchPassword");
            errors.rejectValue("passwordConfirm", "changePassword.unmatchPassword");
        }
    }
}
