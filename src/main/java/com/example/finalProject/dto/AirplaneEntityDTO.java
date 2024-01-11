package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AirplaneEntityDTO {
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    String code;
    @NotNull
    private UUID companyId;
}
