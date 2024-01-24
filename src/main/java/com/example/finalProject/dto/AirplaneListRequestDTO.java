package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
public class AirplaneListRequestDTO {

    DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

    String fromAirport;

    String toAirport;

    int capacity;

    String airplaneClass;

    Time fromTime = Time.valueOf("00:00:00");

    Time toTime = Time.valueOf("23:59:59");

    int fromPrice = 0;

    int toPrice = 100000000;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    Date departureDate;

    String maskapai;
}
