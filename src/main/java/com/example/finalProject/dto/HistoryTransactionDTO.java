package com.example.finalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HistoryTransactionDTO {
    private String orderCode;
    private Integer totalPrice;
    private String departureCode;
    private String departureCityCode;
    private String arrivalCode;
    private String arrivalCityCode;
    private String transactionStatus;
}
