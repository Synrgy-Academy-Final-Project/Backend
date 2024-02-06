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
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleFlightDTO {
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
    @NotNull
    private UUID airplaneFlightTimeId;
    @NotNull
    private Time flightTime;
    @NotBlank
    private String departureCode;
    @NotBlank
    private String departureCityCode;
    @NotBlank
    private String arrivalCode;
    @NotBlank
    private String arrivalCityCode;
    @NotNull
    private Timestamp departureTime;
    @NotNull
    private Timestamp arrivalTime;
    @NotNull
    @Positive
    private Integer totalPrice;
    @PositiveOrZero
    @NotNull
    Integer baggage;

    @PositiveOrZero
    @NotNull
    Integer cabinBaggage;

    @NotNull
    Boolean meals;

    @NotNull
    Boolean travelInsurance;

    @NotNull
    Boolean inflightEntertainment;

    @NotNull
    Boolean electricSocket;

    @NotNull
    Boolean wifi;

    @NotNull
    Boolean reschedule;

    @NotNull
    @PositiveOrZero
    Integer refund;

}
