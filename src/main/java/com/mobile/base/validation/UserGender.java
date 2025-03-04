package com.mobile.base.validation;

import com.mobile.base.validation.impl.UserGenderValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserGenderValidation.class)
@Documented
public @interface UserGender {
    boolean allowNull() default false;

    String message() default "Invalid user gender. Allowed values: 1 (male), 2 (female), 3 (unknown)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
