package com.example.finalProject.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
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
    private String ticketNumber;
    private String gate;
    private String seat;

    public ReportETicketDTO(String companyName, String companyUrl, String airplaneClass, String airplaneCode, String orderCode, Time departureTime, Date departureDate, Time arrivalTime, Date arrivalDate, String departureCityCode, String departureAirportName, String departureCountry, String arrivalCityCode, String arrivalAirportName, String arrivalCountry, String passengerName, String ticketNumber, String gate, String seat) {
        this.companyName = companyName;
        this.companyUrl = companyUrl;
        this.airplaneClass = airplaneClass;
        this.airplaneCode = airplaneCode;
        this.orderCode = orderCode;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.arrivalTime = arrivalTime;
        this.arrivalDate = arrivalDate;
        this.departureCityCode = departureCityCode;
        this.departureAirportName = departureAirportName;
        this.departureCountry = departureCountry;
        this.arrivalCityCode = arrivalCityCode;
        this.arrivalAirportName = arrivalAirportName;
        this.arrivalCountry = arrivalCountry;
        this.passengerName = passengerName;
        this.ticketNumber = ticketNumber;
        this.gate = gate;
        this.seat = seat;
    }
}
