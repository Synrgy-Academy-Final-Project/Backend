package com.example.finalProject.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AirportSearchDTO {
    private String airportCityCode;
    private String airportCityCountry;
}
