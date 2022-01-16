package com.sample.domain.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 입력체크（전각 가나）
 */
@Documented
@Constraint(validatedBy = { ZenKanaValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
@interface ZenKana {

    String message() default "{validator.annotation.ZenKana.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ZenKana[] value();
    }
}
