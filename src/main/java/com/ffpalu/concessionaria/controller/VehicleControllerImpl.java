package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.VehicleController;
import com.ffpalu.concessionaria.dto.request.VehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.exceptions.VehicleException;
import com.ffpalu.concessionaria.middleware.interfaces.VehicleMiddleware;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VehicleControllerImpl implements VehicleController {

    private final VehicleMiddleware vehicleMiddleware;

    @Override
    public ResponseEntity<VehicleResponse> createVehicle(VehicleRequest vehicle) {
        VehicleResponse response = vehicleMiddleware.createVehicle(vehicle);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<VehicleResponse> updateVehicle(VehicleRequest vehicle) {
        VehicleResponse response = vehicleMiddleware.updateVehicle(vehicle);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<VehicleResponse>> getAllVehicle(Pageable pageable) {
        Page<VehicleResponse> allVehicle = vehicleMiddleware.getAllVehicle(pageable);
        return ResponseEntity.ok(allVehicle);
    }

    @Override
    public ResponseEntity<VehicleResponse> getVehicleById(String id) {
        VehicleResponse vehicleResponse = vehicleMiddleware.getVehicleById(id);
        return ResponseEntity.ok(vehicleResponse);
    }

    @Override
    public ResponseEntity<Optional<VehicleResponse>> getVehicleByPlate(String plate) {
        Optional<VehicleResponse> response = vehicleMiddleware.getVehicleByPlate(plate);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<VehicleResponse>> getVehicleByModelAndBrand(String model, String brand, Pageable pageable) {
        Page<VehicleResponse> response = vehicleMiddleware.getVehicleByModelAndBrand(model,brand, pageable);
        return ResponseEntity.ok(response);
    }
}
