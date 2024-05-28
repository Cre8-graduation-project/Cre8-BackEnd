package com.gaduationproject.cre8.common.response.error.checkEnumManOrWoman;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckManOrWomanValidator.class)
public @interface CheckManOrWoman {
    String message() default "M 또는 W 둘 중 하나만 입력이 가능합니다";

    Class[] groups() default {};
    Class[] payload() default {};
}
