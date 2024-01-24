package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;

@Data
public class AirplaneListRequestDTO {
    @NotBlank
    String fromAirport;

    @NotBlank
    String toAirport;

    @NotNull
    @Positive
    int capacity;

    @NotBlank
    String airplaneClass;

    Time fromTime = Time.valueOf("00:00:00");

    Time toTime = Time.valueOf("23:59:59");

    int fromPrice = 0;

    int toPrice = 100000000;

    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    Date departureDate;

    String maskapai;
}
