package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AirplaneAdditionalServiceDTO {
    UUID id;

    @NotNull
    UUID airplaneId;

    @NotBlank
    String type;

    @PositiveOrZero
    Integer quantity;

    @NotNull
    @PositiveOrZero
    Integer price;
}
