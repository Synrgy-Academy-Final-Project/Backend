package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TicketEntityDTO {
    private UUID id;

    @NotBlank
    String seat;

    @NotBlank
    String gate;

    @NotNull
    UUID transactionId;
}
