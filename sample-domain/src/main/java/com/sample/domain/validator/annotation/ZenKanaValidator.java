package com.sample.domain.validator.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * 입력체크（전각 가나）
 */
public class ZenKanaValidator implements ConstraintValidator<ZenKana, String> {

    static final Pattern p = Pattern.compile("^[ァ-ヶ]+$");

    @Override
    public void initialize(ZenKana ZenKana) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = false;

        if (StringUtils.isEmpty(value)) {
            isValid = true;
        } else {
            Matcher m = p.matcher(value);

            if (m.matches()) {
                isValid = true;
            }
        }

        return isValid;
    }
}
