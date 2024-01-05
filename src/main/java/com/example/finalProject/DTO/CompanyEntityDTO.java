package com.example.finalProject.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CompanyEntityDTO {
    private UUID id;
    @NotNull
    private String name;
}
