package com.example.finalProject.controller;

import com.example.finalProject.dto.FlightEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.FlightImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    FlightImpl flightImpl;
    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> searchFlight(@RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "100") int pageSize,
                                                    @RequestParam(defaultValue = "") String sortBy,
                                                    @ModelAttribute("fromAirportCode") String fromAirportCode,
                                                    @ModelAttribute("toAirportCode") String toAirportCode,
                                                    @ModelAttribute("departureDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date departureDate,
                                                    @ModelAttribute("capacity") String capacity,
                                                    @ModelAttribute("airplaneClass") String airplaneClass){
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = flightImpl.searchAll(fromAirportCode, toAirportCode, departureDate, Integer.parseInt(capacity), airplaneClass, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addFlight(@RequestBody @Validated FlightEntityDTO flight){
        ResponseDTO result = flightImpl.save(flight);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findFlight(@PathVariable UUID id){
        ResponseDTO result = flightImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateFlight(@PathVariable UUID id, @RequestBody  FlightEntityDTO flight){
        ResponseDTO result = flightImpl.update(id, flight);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleteFlight(@PathVariable UUID id){
        ResponseDTO result = flightImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
