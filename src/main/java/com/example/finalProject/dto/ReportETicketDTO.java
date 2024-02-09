package com.example.finalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

@AllArgsConstructor
@Data
@Builder
public class ReportETicketDTO {
    private String companyName;
    private String companyUrl;
    private String airplaneClass;
    private String airplaneCode;
    private String orderCode;
    private Time departureTime;
    private Date departureDate;
    private Time arrivalTime;
    private Date arrivalDate;
    private String departureCityCode;
    private String departureAirportName;
    private String departureCountry;
    private String arrivalCityCode;
    private String arrivalAirportName;
    private String arrivalCountry;
    private String passengerName;
    private Integer baggage;
    private Integer additionalBaggage;
    private String airplaneNameCodeDate;
    private Integer priceFlight;
    private Integer seatBaby;
    private Integer totalBabyTransaction;
    private Integer seatMature;
    private Integer totalMatureTransaction;
    private Long qtyItem5;
    private Integer priceItem5;
    private Long subTotPriceItem5;
    private Long qtyItem10;
    private Integer priceItem10;
    private Long subTotPriceItem10;
    private Long qtyItem15;
    private Integer priceItem15;
    private Long subTotPriceItem15;
    private Long qtyItem20;
    private Integer priceItem20;
    private Long subTotPriceItem20;
    private String codePromo;
    private Integer totalDiscount;
    private String discount;
    private Integer total;

}
