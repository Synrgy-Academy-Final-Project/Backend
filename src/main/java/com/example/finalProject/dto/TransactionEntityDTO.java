package com.example.finalProject.dto;

import com.example.finalProject.model.user.UserDetails;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Time;
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

    Date departureDate;

    Time departureTime;

    @NotBlank
    String arrivalCode;

    Date arrivalDate;

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
