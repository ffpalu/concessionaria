package com.ffpalu.concessionaria.dto.validation;

import jakarta.validation.groups.Default;

public class RegistrationValidationGroup {

    public interface RegistrationSeller extends Default {}

    public interface RegistrationOther extends Default {}

}
