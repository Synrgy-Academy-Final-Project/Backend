package com.example.finalProject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class MidtransResponseDTO {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    Date transaction_time;

    String transaction_status;

    UUID transaction_id;

    String status_message;

    @NotNull
    int status_code;

    String signature_key;

    String payment_type;

    UUID order_id;

    String merchant_id;

    String gross_amount;

    String fraud_status;

    String currency;
}
