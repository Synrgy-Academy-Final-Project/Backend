package com.example.finalProject.dto;

import com.example.finalProject.entity.AirplaneClass;
import com.example.finalProject.entity.AirplaneFlightTime;
import com.example.finalProject.entity.Company;
//import com.example.finalProject.entity.Flight;
import lombok.Data;

import java.util.UUID;

@Data
public class AirplaneListDTO {
    UUID id;

    String name;

    String code;

    private Integer airplanePrice;

    private Integer totalPrice;

    private Company company;

    AirplaneClass airplaneClass;

    AirplaneFlightTime airplaneFlightTimes;
}
