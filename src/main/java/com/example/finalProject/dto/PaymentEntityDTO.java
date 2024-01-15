package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentEntityDTO {
    private UUID id;

    @NotBlank
    String accountName;

    @NotBlank
    String accountNumber;

    @NotBlank
    String bankName;
}
