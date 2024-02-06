package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
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
    private UUID airplaneClassId;
    @NotBlank
    private String airplaneClass;
    @NotNull
    private Integer capacity;
    private AirplaneServiceDTO airplaneServices;
    @NotNull
    private UUID airplaneFlightTimeId;
    @NotNull
    private Time flightTime;
    @NotBlank
    private String departureCode;
    @NotBlank
    private String departureCityCode;
    @NotBlank
    private String departureNameAirport;
    @NotBlank
    private String arrivalCode;
    @NotBlank
    private String arrivalCityCode;
    @NotBlank
    private String arrivalNameAirport;
    @NotNull
    private Timestamp departureTime;
    @NotNull
    private Timestamp arrivalTime;
    @NotNull
    @Positive
    private Integer totalPrice;

}
