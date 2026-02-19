package com.ffpalu.concessionaria.controller.interfaces;

import com.ffpalu.concessionaria.dto.request.RegistrationVehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.dto.validation.VehicleValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/vehicle")
public interface VehicleController {

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/create")
    ResponseEntity<VehicleResponse> createVehicle(
            @RequestBody
            @Validated(VehicleValidation.Creation.class)
            RegistrationVehicleRequest vehicle
    );

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update")
    ResponseEntity<VehicleResponse> updateVehicle(
            @RequestBody
            @Validated(VehicleValidation.Modify.class)
            RegistrationVehicleRequest vehicle
    );


    @GetMapping("/")
    ResponseEntity<Page<VehicleResponse>> getAllVehicle(Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<VehicleResponse> getVehicleById(
            @PathVariable
            @NotBlank
            String id
    );

    @GetMapping("/search/{plate}")
    ResponseEntity<Optional<VehicleResponse>> getVehicleByPlate(
            @PathVariable
            @NotBlank
            String plate
    );

    @GetMapping("/search")
    ResponseEntity<Page<VehicleResponse>> getVehicleByModelAndBrand(
            @RequestParam String model,
            @RequestParam String brand,
            Pageable pageable
    );




}
