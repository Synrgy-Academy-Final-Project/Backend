package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TicketEntityDTO {
    private UUID id;

    @NotNull
    String seat;

    @NotNull
    String gate;

    @NotNull
    UUID transactionId;
}
