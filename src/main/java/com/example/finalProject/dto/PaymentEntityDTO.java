package com.example.finalProject.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
