package com.example.finalProject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.UUID;

@Getter
@Setter
@Data
public class AirplaneConfirmDTO {
    public AirplaneConfirmDTO(UUID companyId, String companeName, String urlCompany, UUID airplaneId, String airplaneName, String airplaneCode, UUID airplaneClassId, String airplaneClass, Integer capacity, UUID aiplaneFlightTimeId, Time flghtTime) {
        this.companyId = companyId;
        this.companeName = companeName;
        this.urlCompany = urlCompany;
        this.airplaneId = airplaneId;
        this.airplaneName = airplaneName;
        this.airplaneCode = airplaneCode;
        this.airplaneClassId = airplaneClassId;
        this.airplaneClass = airplaneClass;
        this.capacity = capacity;
        this.airplaneFlightTimeId = aiplaneFlightTimeId;
        this.flghtTime = flghtTime;
    }

    private UUID companyId;
    private String companeName;
    private String urlCompany;
    private UUID airplaneId;
    private String airplaneName;
    private String airplaneCode;
    private UUID airplaneClassId;
    private String airplaneClass;
    private Integer capacity;
    private UUID airplaneFlightTimeId;
    private Time flghtTime;

}
