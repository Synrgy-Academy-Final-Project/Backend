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
public class AirplaneFlightTimeDTO {
    private UUID id;

    @NotNull
    private UUID airplaneId;
    @NotBlank
    private String flightTime;
    @NotNull
    @Positive
    private Integer airplaneFlightTimePrice;

}
