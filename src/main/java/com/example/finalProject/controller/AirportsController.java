package com.example.finalProject.controller;

import com.example.finalProject.DTO.AirportsEntityDTO;
import com.example.finalProject.service.AirportsImpl;
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
public class AirportsController {
    @Autowired
    AirportsImpl airportsImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> searchAirports(@RequestParam(defaultValue = "0") int pageNumber,
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
        return new ResponseEntity<>(airportsImpl.searchAll(code, name, pageable), HttpStatus.OK);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Map<String, Object>> addAirports(@RequestBody @Validated AirportsEntityDTO airports) {
        return new ResponseEntity<>(airportsImpl.save(airports), HttpStatus.OK);
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> findAirports(@PathVariable UUID id) {
        return new ResponseEntity<>(airportsImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> updateAirports(@PathVariable UUID id,
            @RequestBody AirportsEntityDTO airports) {
        return new ResponseEntity<>(airportsImpl.update(id, airports), HttpStatus.OK);
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> deleteAirports(@PathVariable UUID id) {
        return new ResponseEntity<>(airportsImpl.delete(id), HttpStatus.OK);
    }
}
