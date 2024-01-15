package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private UUID companyId;
}
