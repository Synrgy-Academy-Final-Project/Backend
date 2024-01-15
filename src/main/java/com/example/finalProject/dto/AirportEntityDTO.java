package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class AirportEntityDTO {

    private UUID id;

    @NotBlank
    private String name;

    @NotNull
    @Size(min = 3, max = 3)
    String code;

    @NotBlank
    String city;

    @NotBlank
    String country;
}
