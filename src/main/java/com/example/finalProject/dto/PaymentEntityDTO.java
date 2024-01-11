package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentEntityDTO {
    private UUID id;

    @NotNull
    String accountName;

    @NotNull
    String accountNumber;

    @NotNull
    String bankName;
}
