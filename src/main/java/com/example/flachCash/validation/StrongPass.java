package com.example.flachCash.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPassValidator.class)
public @interface StrongPass {

    String message() default " the password must contain a least 8 characters, uppercase and lowercase, number and special character.";

    Class<?> [] groups() default {};
    Class<?> [] payload() default {};






}
