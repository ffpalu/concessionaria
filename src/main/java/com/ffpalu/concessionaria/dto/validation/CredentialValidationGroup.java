package com.ffpalu.concessionaria.dto.validation;

import jakarta.validation.groups.Default;

public class CredentialValidationGroup {

    public interface Create extends Default {}

    public interface ChangePassword extends Default {}

    public interface Login extends Default{}

}
