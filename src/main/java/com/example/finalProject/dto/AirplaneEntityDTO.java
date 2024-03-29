package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class AirplaneEntityDTO {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    String code;
    @NotNull
    @Positive
    private Integer airplanePrice;
    @NotNull
    private UUID companyId;
}
