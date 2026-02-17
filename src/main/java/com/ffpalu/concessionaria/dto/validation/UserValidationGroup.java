package com.ffpalu.concessionaria.dto.validation;

import jakarta.validation.groups.Default;

public class UserValidationGroup {
    public interface Creation extends Default {}

    public interface Modify {}
}
