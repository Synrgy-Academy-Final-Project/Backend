package com.example.finalProject.controller;

import com.example.finalProject.dto.AirplaneEntityDTO;
import com.example.finalProject.dto.AirplaneListRequestDTO;
import com.example.finalProject.dto.AirplaneServiceEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.AirplaneImpl;
import com.example.finalProject.service.AirplaneServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/airplane-service")
@Slf4j
public class AirplaneServiceController {
    @Autowired
    AirplaneServiceImpl airplaneServiceImpl;

    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> searchAirplaneService(@RequestParam(defaultValue = "0") int pageNumber,
                                                             @RequestParam(defaultValue = "100") int pageSize,
                                                             @RequestParam(defaultValue = "") String sortBy){
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = airplaneServiceImpl.searchAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addAirplaneService(@RequestBody @Validated AirplaneServiceEntityDTO airplaneService){
        ResponseDTO result = airplaneServiceImpl.save(airplaneService);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findAirplaneService(@PathVariable UUID id){
        ResponseDTO result = airplaneServiceImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateAirplaneService(@PathVariable UUID id, @RequestBody AirplaneServiceEntityDTO airplaneService){
        ResponseDTO result = airplaneServiceImpl.update(id, airplaneService);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleteAirplaneService(@PathVariable UUID id){
        ResponseDTO result = airplaneServiceImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
