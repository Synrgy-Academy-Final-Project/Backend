package com.example.finalProject.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
