package com.example.flachCash.validation;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;


import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch,Object> {

    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.firstField = constraintAnnotation.first();
        this.secondField = constraintAnnotation.second();

    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try{
            String firstValue = getFieldValue(value,firstField);
            String secondValue = getFieldValue(value,secondField);

            return firstValue != null && Objects.equals(firstValue,secondValue);

        } catch (Exception e) {
            return false;
        }

    }

    private String getFieldValue(Object value, String firstName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return BeanUtils.getProperty(value, firstName);
    }
}
