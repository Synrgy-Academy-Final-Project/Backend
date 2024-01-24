package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Data
public class AirplaneListRequestDTO {
    String fromAirport;
    String toAirport;
    int capacity;
    String airplaneClass;
    Time fromTime;
    Time toTime;
    int fromPrice;
    int toPrice;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    Date departureDate;

    String maskapai;
}
