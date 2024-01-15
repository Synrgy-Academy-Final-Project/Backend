package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class FlightEntityDTO {
    UUID id;

    @NotNull
    UUID airplaneId;

    @NotNull
    Date departureDate;

    @NotNull
    Date arrivalDate;

    @NotNull
    @Positive
    Integer capacity;

    @NotBlank
    String airplaneClass;

    @NotNull
    UUID fromAirportId;

    @NotNull
    UUID toAirportId;

    @NotNull
    @Positive
    Integer price;

    String fromAirportCode;

    String toAirportCode;
}
