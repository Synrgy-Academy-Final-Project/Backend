package com.example.finalProject.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentMidtransReponse {
    String token;
    String redirect_url;
    UUID orderId;
}
