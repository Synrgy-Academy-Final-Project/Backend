package com.example.finalProject.controller;

import com.example.finalProject.dto.AirplaneFlightTimeDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.airplaneFlightTime.AirplaneFlightTimeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@RequestMapping("/airplaneflighttime")
@Slf4j
public class AirplaneFlightTimeController {
    private final AirplaneFlightTimeServiceImpl airplaneFlightTimeService;

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> add(@RequestBody @Validated AirplaneFlightTimeDTO request){
        ResponseDTO save = airplaneFlightTimeService.save(request);
        return new ResponseEntity<>(save, HttpStatus.valueOf(save.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findbyId(@PathVariable UUID id){
        ResponseDTO result = airplaneFlightTimeService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> update(@PathVariable UUID id, @RequestBody @Validated AirplaneFlightTimeDTO request){
        ResponseDTO result = airplaneFlightTimeService.update(id, request);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping ({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleted(@PathVariable UUID id){
        ResponseDTO result = airplaneFlightTimeService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"","/"})
    public ResponseEntity<ResponseDTO> searchAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "100") int pageSize,
            @RequestParam(defaultValue = "") String sortBy,
            @ModelAttribute("airplaneClass") String airplaneClass,
            @ModelAttribute("airplanePrice") String airplanePrice
    ){
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = airplaneFlightTimeService.searchAll(airplaneClass, airplanePrice, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
