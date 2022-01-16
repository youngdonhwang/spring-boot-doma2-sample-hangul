package com.sample.web.admin.controller.html.system.staffs;

import static com.sample.common.util.ValidateUtils.isNotEquals;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 담당자등록 입력체크
 */
@Component
public class StaffFormValidator extends AbstractValidator<StaffForm> {

    @Override
    protected void doValidate(StaffForm form, Errors errors) {

        // 확인용 패스워드와 동일한지 확인한다
        if (isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
            errors.rejectValue("password", "staffs.unmatchPassword");
            errors.rejectValue("passwordConfirm", "staffs.unmatchPassword");
        }
    }
}
