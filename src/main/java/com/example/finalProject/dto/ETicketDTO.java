package com.example.finalProject.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class ETicketDTO {
    private String fullName;
    private String airplaneName;
    private String airplaneCode;
    private String airplaneClass;
    private Date departureDate;
    private String airportNameDeparture;
    private String airportCodeDeparture;
    private String airportCityDeparture;
    private String airportCountryDeparture;
    private Date arrivalDate;
    private String airportNameArrival;
    private String airportCodeArrival;
    private String airportCityArrival;
    private String airportCountryArrival;
    private UUID ticketNumber;
    private String gate;
    private String seat;

}
