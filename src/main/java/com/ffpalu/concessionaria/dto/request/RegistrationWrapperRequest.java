package com.ffpalu.concessionaria.dto.request;

import com.ffpalu.concessionaria.dto.validation.RegistrationValidationGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationWrapperRequest {

    @Valid
    private UserDetailsRequest user;

    @Valid
    private CredentialRequest credential;

    @NotNull(groups = RegistrationValidationGroup.RegistrationSeller.class, message = "Dettagli necessari per la registrazione di un venditore")
    @Valid
    private SellerDetailsRequest details;

}
