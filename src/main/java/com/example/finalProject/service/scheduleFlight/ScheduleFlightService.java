package com.example.finalProject.service.scheduleFlight;

import com.example.finalProject.dto.ResponseDTO;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.Date;

public interface ScheduleFlightService {
//    ResponseDTO getScheduleFlight(String departureCode, String arrivalCode, Date departureDate, String airplaneClass, Pageable pageable);

//    ResponseDTO getScheduleFlight(String departureCode, String arrivalCode, Date departureDate, String airplaneClass,
//                                  String departureTimeFilter, String companyName, String hasBaggage, String hasInflightEntertainment, String hasMeals,
//                                  String hasUSB, String hasWIFI, String hasRefund, String hasReschedule, Pageable pageable);
    ResponseDTO getScheduleFlight(String departureCode, String arrivalCode, Date departureDate, String airplaneClass,
                                  String departureTimeFilter, String companyName, String hasBaggage, String hasInflightEntertainment, String hasMeals,
                                  String hasUSB, String hasWIFI, String hasRefund, String hasReschedule, String fromPrice, String toPrice, Pageable pageable);
}
