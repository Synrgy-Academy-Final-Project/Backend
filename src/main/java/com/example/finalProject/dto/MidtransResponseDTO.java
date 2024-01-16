package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class MidtransResponseDTO {
    @NotNull
    LocalDateTime transaction_time;
    @NotBlank
    String transaction_status;
    @NotNull
    UUID transaction_id;
    @NotBlank
    String status_message;
    @NotNull
    int status_code;
    @NotBlank
    String signature_key;
    @NotBlank
    String payment_type;
    @NotBlank
    String order_id;
    @NotBlank
    String merchant_id;
    @NotNull
    int gross_amount;
    @NotBlank
    String fraud_status;
    @NotBlank
    String currency;
}
