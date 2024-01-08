package com.example.finalProject.controller;

import com.example.finalProject.dto.AirportEntityDTO;
import com.example.finalProject.service.AirportImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/airports")
@Slf4j
public class AirportController {
    @Autowired
    AirportImpl airportImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> searchAirport(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "100") int pageSize,
            @RequestParam(defaultValue = "") String sortBy,
            @ModelAttribute("name") String name,
            @ModelAttribute("code") String code) {
        Pageable pageable;
        if (sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNumber, pageSize);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        return new ResponseEntity<>(airportImpl.searchAll(code, name, pageable), HttpStatus.OK);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, Object>> addAirport(@RequestBody @Validated AirportEntityDTO airport) {
        return new ResponseEntity<>(airportImpl.save(airport), HttpStatus.OK);
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> findAirport(@PathVariable UUID id) {
        return new ResponseEntity<>(airportImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> updateAirport(@PathVariable UUID id,
            @RequestBody AirportEntityDTO airport) {
        return new ResponseEntity<>(airportImpl.update(id, airport), HttpStatus.OK);
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> deleteAirport(@PathVariable UUID id) {

        return new ResponseEntity<>(airportImpl.delete(id), HttpStatus.OK);
    }
}