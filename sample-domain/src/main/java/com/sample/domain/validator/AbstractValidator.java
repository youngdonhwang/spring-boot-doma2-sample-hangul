package com.sample.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

/**
 * 기저 입력 체크 클래스
 */
@Slf4j
public abstract class AbstractValidator<T> implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(final Object target, final Errors errors) {
        try {
            boolean hasErrors = errors.hasErrors();

            if (!hasErrors || passThruBeanValidation(hasErrors)) {
                // 각 기능에서 구현하고 있는 유효성 검증을 실행한다
                doValidate((T) target, errors);
            }
        } catch (RuntimeException e) {
            log.error("validate error", e);
            throw e;
        }
    }

    /**
     * 입력 체크를 실시한다.
     *
     * @param form
     * @param errors
     */
    protected abstract void doValidate(final T form, final Errors errors);

    /**
     * 상호 관계 체크 유효성 검증을 실시할 지의 여부를 나타내는 값을 반환한다.<br />
     * 디폴트는 JSR-303 유효성 검증에서 오류가 있을 경우에 상호 관련 체크를 실시하지 않는다.
     * 
     * @return
     */
    protected boolean passThruBeanValidation(boolean hasErrors) {
        return false;
    }
}
