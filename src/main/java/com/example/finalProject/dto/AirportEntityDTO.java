package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class AirportEntityDTO {

    private UUID id;

    @NotNull
    private String name;

    @NotNull
    String code;

    @NotNull
    String city;

    @NotNull
    String country;
}
