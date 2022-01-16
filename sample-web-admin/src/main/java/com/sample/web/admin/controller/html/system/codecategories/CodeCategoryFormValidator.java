package com.sample.web.admin.controller.html.system.codecategories;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 코드분류등록 입력 체크
 */
@Component
public class CodeCategoryFormValidator extends AbstractValidator<CodeCategoryForm> {

    @Override
    protected void doValidate(CodeCategoryForm form, Errors errors) {

    }
}
