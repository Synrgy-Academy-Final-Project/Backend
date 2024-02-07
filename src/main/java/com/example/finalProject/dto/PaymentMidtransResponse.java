package com.example.finalProject.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentMidtransResponse {
    String token;
    String redirectUrl;
    UUID orderId;
}
