package com.codecool.companymanager.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements
        ConstraintValidator<ContactNumberConstraint, String> {
    @Override
    public void initialize(ContactNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value.matches("[\\d() +-]+")
                && (value.length() > 8) && (value.length() < 16);
    }
}
