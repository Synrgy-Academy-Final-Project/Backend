package com.example.finalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AirplaneAdditionalBaggageDTO {
    private UUID airplaneId;
    private String type;
    private Integer qty;
    private Integer price;
}
