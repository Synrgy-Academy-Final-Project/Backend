package com.example.finalProject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
public class TotalSeatDTO {

    public TotalSeatDTO(Long totalSeatTransaction, UUID airplaneIdTrans, UUID airplaneClassIdTrans, UUID airplaneTimeFlightIdTrans) {
        this.totalSeatTransaction = totalSeatTransaction;
        this.airplaneIdTrans = airplaneIdTrans;
        this.airplaneClassIdTrans = airplaneClassIdTrans;
        this.airplaneTimeFlightIdTrans = airplaneTimeFlightIdTrans;
    }

    Long totalSeatTransaction;
    UUID airplaneIdTrans;
    UUID airplaneClassIdTrans;
    UUID airplaneTimeFlightIdTrans;
}
