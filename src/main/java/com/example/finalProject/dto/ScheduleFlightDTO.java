package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleFlightDTO {
    @NotBlank
    private String companyName;
    @NotBlank
    private String urlLogo;
    @NotBlank
    private String airplaneName;
    @NotBlank
    private String airplaneCode;
    @NotBlank
    private String airplaneClass;
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
