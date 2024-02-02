package com.example.finalProject.controller;

import com.example.finalProject.dto.AirportEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.AirportImpl;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/airports")
@Slf4j
public class AirportController {
    private final AirportImpl airportImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> searchAirport(@RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "100") int pageSize,
                                                     @RequestParam(defaultValue = "") String sortBy,
                                                     @ModelAttribute("query") String query
    ) {
        Pageable pageable;
        if (sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNumber, pageSize);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = airportImpl.searchAll(query, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> addAirport(@RequestBody @Validated AirportEntityDTO airport) {
        ResponseDTO result = airportImpl.save(airport);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> findAirport(@PathVariable UUID id) {
        ResponseDTO result = airportImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> updateAirport(@PathVariable UUID id,
                                                     @RequestBody AirportEntityDTO airport) {
        ResponseDTO result = airportImpl.update(id, airport);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> deleteAirport(@PathVariable UUID id) {
        ResponseDTO result = airportImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
