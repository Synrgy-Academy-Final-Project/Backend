package com.example.finalProject.controller;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.scheduleFlight.ScheduleFlightServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/scheduleflight")
@Slf4j
public class ScheduleFlightController {
    private final ScheduleFlightServiceImpl scheduleFlightService;

    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> getFlight(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "100") int pageSize,
                                                 @RequestParam(defaultValue = "") String sortBy,
                                                 @ModelAttribute("departureCode") String fromAirportCode,
                                                 @ModelAttribute("arrivalCode") String toAirportCode,
                                                 @ModelAttribute("departureDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date departureDate,
                                                 @ModelAttribute("airplaneClass") String airplaneClass) throws ParseException {
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = scheduleFlightService.getScheduleFlight(fromAirportCode, toAirportCode, departureDate, airplaneClass, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
