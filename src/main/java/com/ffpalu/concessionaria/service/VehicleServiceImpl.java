package com.ffpalu.concessionaria.service;

import com.ffpalu.concessionaria.dto.request.VehicleRequest;
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
    public Vehicle createVehicle(VehicleRequest vehicleRequest) {
        if (vehicleRepository.existsByPlate(vehicleRequest.getPlate())) {
            throw new VehicleException("Vehicle already insert");
        }

        Vehicle vehicle = mapper.mapToVehicle(vehicleRequest);

        vehicle = vehicleRepository.save(vehicle);

        return vehicle;


    }

    @Override
    public Optional<Vehicle> getVehicleByPlate(String plate) {
        return vehicleRepository.findByPlate(plate);
    }

    @Override
    public Vehicle getVehicleById(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleException("Vehicle not found"));
    }


    @Override
    public Page<Vehicle> getVehicleFromModelAndBrand(String model, String brand, Pageable pageable) {
        return vehicleRepository.findByModelAndBrand(model, brand, pageable);
    }

    @Override
    public Page<Vehicle> getAllVehicle(Pageable pageable) {
        return vehicleRepository.findAll(pageable);
    }


    @Override
    public Vehicle updateVehicle(VehicleRequest vehicleRequest) {

        if (vehicleRequest.getId() == null) {
            throw new VehicleException("Vehicle vehicle request to update must have an ID");
        }

        Vehicle vehicleUpdate = vehicleRepository.findById(UUID.fromString(vehicleRequest.getId()))
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


}
