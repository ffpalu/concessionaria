package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.RegistrationVehicleRequest;
import com.ffpalu.concessionaria.dto.response.VehicleResponse;
import com.ffpalu.concessionaria.entity.Vehicle;
import com.ffpalu.concessionaria.exceptions.VehicleException;
import com.ffpalu.concessionaria.repository.VehicleRepository;
import com.ffpalu.concessionaria.service.interfaces.VehicleService;
import com.ffpalu.concessionaria.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final Mapper mapper;
    private final VehicleRepository vehicleRepository;

    @Override
    public VehicleResponse createVehicle(RegistrationVehicleRequest vehicleRequest) {
        if (vehicleRepository.existsByPlate(vehicleRequest.getPlate())) {
            throw new VehicleException("Vehicle already insert");
        }

        Vehicle vehicle = mapper.mapToVehicle(vehicleRequest);

        vehicle = vehicleRepository.save(vehicle);

        return mapper.mapToVehicleResponse(vehicle);


    }

    @Override
    public Optional<Vehicle> getVehicleByPlate(String plate) {
        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> getVehicleById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<VehicleResponse> getVehicleResponseByPlate(String plate) {
        return getVehicleByPlate(plate).map(mapper::mapToVehicleResponse);
    }

    @Override
    public Optional<VehicleResponse> getVehicleResponseById(UUID id) {
        return getVehicleById(id).map(mapper::mapToVehicleResponse);
    }

    @Override
    public Page<Vehicle> getVehicleFromModelAndBrand(String model, String brand, Pageable pageable) {
        return vehicleRepository.findByModelAndBrand(model, brand, pageable);
    }

    @Override
    public Page<VehicleResponse> getVehicleResponseFromModelAndBrand(String model, String brand, Pageable pageable) {
        return getVehicleFromModelAndBrand(model, brand, pageable)
                .map(mapper::mapToVehicleResponse);
    }

    @Override
    public Page<Vehicle> getAllVehicle(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }

    @Override
    public Page<VehicleResponse> getAllVehicleResponse(Pageable pageable) {
        return getAllVehicle(pageable).map(mapper::mapToVehicleResponse);
    }

    @Override
    public Vehicle updateVehicle(RegistrationVehicleRequest vehicleRequest) {

        if (vehicleRequest.getId() == null) {
            throw new VehicleException("Vehicle vehicle request to update must have an ID");
        }

        Vehicle vehicleUpdate = vehicleRepository.findById(vehicleRequest.getId())
                .orElseThrow(() -> new VehicleException("Vehicle not found"));

        vehicleUpdate = Vehicle.builder()
                .id(vehicleUpdate.getId())
                .plate(vehicleRequest.getPlate() != null ? vehicleRequest.getPlate() : vehicleUpdate.getPlate())
                .brand(vehicleRequest.getBrand() != null ? vehicleRequest.getBrand() : vehicleUpdate.getBrand())
                .model(vehicleRequest.getModel() != null ? vehicleRequest.getModel() : vehicleUpdate.getModel())
                .used(vehicleRequest.getUsed() != null ? vehicleRequest.getUsed() : vehicleUpdate.getUsed())
                .year(vehicleRequest.getYear() != null ? vehicleRequest.getYear() : vehicleUpdate.getYear())
                .numberOfKilometers(vehicleRequest.getNumberOfKilometers() != null ? vehicleRequest.getNumberOfKilometers() : vehicleUpdate.getNumberOfKilometers())
                .build();

        return vehicleRepository.save(vehicleUpdate);

    }

    @Override
    public VehicleResponse updateVehicleResponse(RegistrationVehicleRequest vehicleRequest) {
        return mapper.mapToVehicleResponse(updateVehicle(vehicleRequest));
    }

}
