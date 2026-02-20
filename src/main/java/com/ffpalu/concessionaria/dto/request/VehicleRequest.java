package com.ffpalu.concessionaria.dto.request;

import com.ffpalu.concessionaria.annotation.interfaces.MinYear;
import com.ffpalu.concessionaria.dto.validation.VehicleValidation;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Year;

@Data
public class VehicleRequest {

    @NotBlank(groups = VehicleValidation.Modify.class)
    private String id;

    @NotBlank(message = "Plate required")
    @Size(min = 2, message = "Plate must be at least 2 char")
    private String plate;

    @NotBlank(message = "must indicate brand")
    private String brand;

    @NotBlank(message = "must indicate model")
    private String model;

    @NotNull(message = "must indicate if vehicle are used")
    private Boolean used;

    @NotNull(message = "must indicate years")
    @PastOrPresent(message = "Year mus be present or past")
    @MinYear(value = 1950, message = "Year min 1950")
    private Year year;

    @PositiveOrZero(message = "number of kilometer must be at least 0")
    private Integer numberOfKilometers;

}
