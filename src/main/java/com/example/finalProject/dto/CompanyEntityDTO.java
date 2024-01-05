package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CompanyEntityDTO {
    private UUID id;
    @NotNull
    private String name;
}
