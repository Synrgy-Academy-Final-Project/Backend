package com.example.finalProject.dto;

import com.example.finalProject.model.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class TransactionEntityDTO {
    UUID id;

    @NotNull
    UUID userId;

    @NotBlank
    String companyName;

    @NotBlank
    String url;

    @NotNull
    UUID airplaneId;

    @NotBlank
    String airplaneName;

    @NotBlank
    String airplaneCode;

    @NotNull
    UUID airplaneClassId;

    @NotBlank
    String airplaneClass;

    @NotNull
    UUID airplaneTimeFLightId;

    @NotBlank
    String departureCode;

    @NotNull
    @JsonFormat(pattern="dd-MM-yyyy")
    LocalDate departureDate;

    Time departureTime;

    @NotBlank
    String arrivalCode;

    LocalDate arrivalDate;

    Time arrivalTime;

    //    @NotNull
//    @Positive
//    Integer totalSeat;
//
    @NotNull
    @Positive
    Integer priceFlight;

    List<UserDetails> userDetails;
}
