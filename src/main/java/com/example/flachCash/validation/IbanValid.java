package com.example.flachCash.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IbanValidator.class)
public @interface IbanValid {
    String message() default "Please enter a valid IbanValid";
    Class<?> [] groups() default {};
    Class<?> [] payload() default {};
}

