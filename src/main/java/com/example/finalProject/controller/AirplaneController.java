package com.example.finalProject.controller;

import com.example.finalProject.dto.AirplaneEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.AirplaneImpl;
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
@CrossOrigin
@RequestMapping("/airplane")
@Slf4j
public class AirplaneController {
    @Autowired
    AirplaneImpl airplaneImpl;

    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> searchAirplane(@RequestParam(defaultValue = "0") int pageNumber,
                                                      @RequestParam(defaultValue = "100") int pageSize,
                                                      @RequestParam(defaultValue = "") String sortBy,
                                                      @ModelAttribute("name") String name,
                                                      @ModelAttribute("code") String code){
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = airplaneImpl.searchAll(code, name, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addAirplane(@RequestBody @Validated AirplaneEntityDTO airplane){
        ResponseDTO result = airplaneImpl.save(airplane);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findAirplane(@PathVariable UUID id){
        ResponseDTO result = airplaneImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateAirplane(@PathVariable UUID id, @RequestBody  AirplaneEntityDTO airplane){
        ResponseDTO result = airplaneImpl.update(id, airplane);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleteAirplane(@PathVariable UUID id){
        ResponseDTO result = airplaneImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
