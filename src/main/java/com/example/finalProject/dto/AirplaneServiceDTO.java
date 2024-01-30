package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AirplaneServiceDTO {
    @PositiveOrZero
    @NotNull
    Integer baggage;

    @PositiveOrZero
    @NotNull
    Integer cabinBaggage;

    @NotNull
    Boolean meals;

    @NotNull
    Boolean travelInsurance;

    @NotNull
    Boolean inflightEntertainment;

    @NotNull
    Boolean electricSocket;

    @NotNull
    Boolean wifi;

    @NotNull
    Boolean reschedule;

    @NotNull
    @PositiveOrZero
    Integer refund;
}
