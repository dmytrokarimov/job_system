package com.example.job_system.dto.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OneOfValidator implements ConstraintValidator<OneOf, Object> {

    private List<String> possibleValues;

    @Override
    public void initialize(OneOf constraintAnnotation) {
        possibleValues = Arrays.asList(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return possibleValues.contains(String.valueOf(value));
    }

}
