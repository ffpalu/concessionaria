package com.ffpalu.concessionaria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegistrationCustomer {

    @NotBlank(message = "First name must be present")
    @Size(min = 3, message = "First name must have at least 3 char")
    private String firstName;


    @NotBlank(message = "Last name must be present")
    @Size(min = 3, message = "First name must have at least 3 char")
    private String lastName;

    @Email(message = "email must have this pattern: abc@cde.xyz")
    private String email;

    @NotBlank(message = "CF must be present")
    @Size(min = 16, max = 16, message = "CF must be 16 char")
    private String CF;

}
