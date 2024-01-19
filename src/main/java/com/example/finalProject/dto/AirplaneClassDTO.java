package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AirplaneClassDTO {

    private UUID id;

    @NotNull
    private UUID airplaneId;

    @NotBlank
    private String airplaneClass;

    @NotNull
    @Positive
    private Integer airplaneClassPrice;
}
