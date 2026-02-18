package com.ffpalu.concessionaria.annotation.interfaces;

import com.ffpalu.concessionaria.annotation.MinYearValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinYearValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinYear {
    int value();

    String message() default "Year must be greater or equal of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
