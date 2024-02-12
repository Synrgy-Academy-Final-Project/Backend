package com.example.finalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class HistoryTransactionDTO {
    private UUID orderId;
    private String urlCompany;
    private String departureCode;
    private String departureTime;
    private String departureDate;
    private String arrivalCode;
    private String arrivalTime;
    private String arrivalDate;
    private String durationAirplane;
    private String departureCityCode;
    private String departureAirportName;
    private String departureCountry;
    private String arrivalCityCode;
    private String arrivalAirportName;
    private String arrivalCountry;
    private String oderCode;
    private String paymentMethod;
    private String transactionStatus;
    private Integer totalPrice;
}
