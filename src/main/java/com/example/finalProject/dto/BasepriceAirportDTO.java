package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class BasepriceAirportDTO {
    private UUID id;

    @NotNull
    private UUID fromAirport;
    @NotNull
    private UUID toAirport;
    private Integer duration;
    @NotNull
    @Positive
    private Integer airportPrice;
}
