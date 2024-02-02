package com.example.finalProject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Data
public class TotalSeatDTO {

    public TotalSeatDTO(Long totalSeatTransaction, Date departureDateTrans, Time departureTimeTrans, Date arrivalDateTrans, Time arrivalTimeTrans, UUID airplaneIdTrans, UUID airplaneClassIdTrans, UUID airplaneTimeFlightIdTrans) {
        this.totalSeatTransaction = totalSeatTransaction;
        this.departureDateTrans = departureDateTrans;
        this.departureTimeTrans = departureTimeTrans;
        this.arrivalDateTrans = arrivalDateTrans;
        this.arrivalTimeTrans = arrivalTimeTrans;
        this.airplaneIdTrans = airplaneIdTrans;
        this.airplaneClassIdTrans = airplaneClassIdTrans;
        this.airplaneTimeFlightIdTrans = airplaneTimeFlightIdTrans;
    }

    Long totalSeatTransaction;
    Date departureDateTrans;
    Time departureTimeTrans;
    Date arrivalDateTrans;
    Time arrivalTimeTrans;
    UUID airplaneIdTrans;
    UUID airplaneClassIdTrans;
    UUID airplaneTimeFlightIdTrans;
}
