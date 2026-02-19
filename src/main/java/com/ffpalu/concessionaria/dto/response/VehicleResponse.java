package com.ffpalu.concessionaria.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

@Data
@Builder
public class VehicleResponse {

    private UUID id;
    private String plate;
    private String model;
    private String brand;
    private boolean used;
    private Year year;
    private Integer numberOfKilometers;
    private boolean sold;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
