package com.example.finalProject.DTO;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class AirportsEntityDTO {

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
