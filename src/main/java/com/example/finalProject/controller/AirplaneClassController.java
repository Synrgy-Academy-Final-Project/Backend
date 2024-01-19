package com.example.finalProject.controller;

import com.example.finalProject.dto.AirplaneClassDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.airplaneClass.AirplaneClassServiceImpl;
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
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/airplaneclass")
@Slf4j
public class AirplaneClassController {
    private final AirplaneClassServiceImpl airplaneClassService;

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> add(@RequestBody @Validated AirplaneClassDTO request){
        ResponseDTO save = airplaneClassService.save(request);
        return new ResponseEntity<>(save, HttpStatus.valueOf(save.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findbyId(@PathVariable UUID id){
        ResponseDTO result = airplaneClassService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> update(@PathVariable UUID id, @RequestBody @Validated AirplaneClassDTO request){
        ResponseDTO result = airplaneClassService.update(id, request);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping ({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleted(@PathVariable UUID id){
        ResponseDTO result = airplaneClassService.delete(id);
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
        ResponseDTO result = airplaneClassService.searchAll(airplaneClass, airplanePrice, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
