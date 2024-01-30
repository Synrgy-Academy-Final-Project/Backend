package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleFlightResponseDTO {
    @NotBlank
    private String companyName;
    @NotBlank
    private String urlLogo;
    @NotNull
    private UUID airplaneId;
    @NotBlank
    private String airplaneName;
    @NotBlank
    private String airplaneCode;
    @NotNull
    private String airplaneClassId;
    @NotBlank
    private String airplaneClass;
    @NotNull
    private Integer capacity;
    private AirplaneServiceDTO airplaneServices;
    @NotNull
    private String airplaneFlightTimeId;
    @NotNull
    private Time flightTime;
    @NotBlank
    private String departureCode;
    @NotBlank
    private String arrivalCode;
    @NotNull
    private Timestamp departureTime;
    @NotNull
    private Timestamp arrivalTime;
    @NotNull
    @Positive
    private Integer totalPrice;

}
