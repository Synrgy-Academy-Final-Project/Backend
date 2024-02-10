package com.example.finalProject.controller;

import com.example.finalProject.dto.AirplaneAdditionalServiceDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.AirplaneAdditionalServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/airplane-additional-service")
@Slf4j
public class AirplaneAdditionalServiceController {
    @Autowired
    AirplaneAdditionalServiceImpl airplaneAdditionalServiceImpl;

    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> searchAirplaneAdditionalService(@RequestParam(defaultValue = "0") int pageNumber,
                                                                       @RequestParam(defaultValue = "100") int pageSize,
                                                                       @RequestParam(defaultValue = "") String sortBy){
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = airplaneAdditionalServiceImpl.searchAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addAirplaneAdditionalService(@RequestBody @Validated AirplaneAdditionalServiceDTO airplaneAdditionalService){
        ResponseDTO result = airplaneAdditionalServiceImpl.save(airplaneAdditionalService);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findAirplaneAdditionalService(@PathVariable UUID id){
        ResponseDTO result = airplaneAdditionalServiceImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateAirplaneAdditionalService(@PathVariable UUID id, @RequestBody AirplaneAdditionalServiceDTO airplaneAdditionalService){
        ResponseDTO result = airplaneAdditionalServiceImpl.update(id, airplaneAdditionalService);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleteAirplaneAdditionalService(@PathVariable UUID id){
        ResponseDTO result = airplaneAdditionalServiceImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"baggage/{airplaneId}", "baggage/{airplaneId}/"})
    public ResponseEntity<ResponseDTO> getAdditionalAirplaneBaggage(@PathVariable UUID airplaneId){
        ResponseDTO result = airplaneAdditionalServiceImpl.getAdditionalBaggage(airplaneId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
