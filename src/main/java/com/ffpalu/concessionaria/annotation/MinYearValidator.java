package com.ffpalu.concessionaria.annotation;

import com.ffpalu.concessionaria.annotation.interfaces.MinYear;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class MinYearValidator implements ConstraintValidator<MinYear, Year> {
    private int minYear;

    @Override
    public boolean isValid(Year year, ConstraintValidatorContext constraintValidatorContext) {
        if (year == null){
            return true;
        }
        return year.getValue() >= minYear;
    }

    @Override
    public void initialize(MinYear constraintAnnotation) {
        this.minYear = constraintAnnotation.value();
    }
}
