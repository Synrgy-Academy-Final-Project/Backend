package com.example.finalProject.controller;

import com.example.finalProject.DTO.AirplaneEntityDTO;
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
@RequestMapping("/airplane")
@Slf4j
public class AirplaneController {
    @Autowired
    AirplaneImpl airplaneImpl;

    @GetMapping({"", "/"})
    public ResponseEntity<Object> searchAirplane(@RequestParam(defaultValue = "0") int pageNumber,
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
        return new ResponseEntity<>(airplaneImpl.searchAll(code, name, pageable), HttpStatus.OK);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Map> addAirplane(@RequestBody @Validated AirplaneEntityDTO airplane){
        return new ResponseEntity<>(airplaneImpl.save(airplane), HttpStatus.OK);
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<Map> findAirplane(@PathVariable UUID id){
        return new ResponseEntity<>(airplaneImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<Map> updateAirplane(@PathVariable UUID id, @RequestBody  AirplaneEntityDTO airplane){
        return new ResponseEntity<>(airplaneImpl.update(id, airplane), HttpStatus.OK);
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<Map> deleteAirplane(@PathVariable UUID id){
        return new ResponseEntity<>(airplaneImpl.delete(id), HttpStatus.OK);
    }
}
