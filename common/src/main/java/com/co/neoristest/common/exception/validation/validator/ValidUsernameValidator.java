package com.co.neoristest.common.exception.validation.validator;

import com.co.neoristest.common.exception.validation.anotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;


public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        String ragex = "[a-zA-Z0-9]+";

        if(Objects.isNull(value)) return false;

        if(value.isEmpty()) return false;

        return value.matches(ragex);
    }
}
