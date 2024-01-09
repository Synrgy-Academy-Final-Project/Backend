package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
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
    Integer capacity;

    @NotNull
    String airplaneClass;

    @NotNull
    UUID fromAirportId;

    @NotNull
    UUID toAirportId;

    @NotNull
    Integer price;

    String fromAirportCode;

    String toAirportCode;
}
