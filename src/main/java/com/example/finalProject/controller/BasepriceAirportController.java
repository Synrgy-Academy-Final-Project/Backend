package com.example.finalProject.controller;

import com.example.finalProject.dto.BasepriceAirportDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.basepriceAirport.BasepriceAirportServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/basepriceairport")
@Slf4j
public class BasepriceAirportController {
    private final BasepriceAirportServiceImpl basepriceAirportService;

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addBasePriceAirport(@RequestBody @Validated BasepriceAirportDTO request){
        ResponseDTO save = basepriceAirportService.save(request);
        return new ResponseEntity<>(save, HttpStatus.valueOf(save.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findBasePriceAirport(@PathVariable UUID id){
        ResponseDTO result = basepriceAirportService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateBasePriceAirport(@PathVariable UUID id, @RequestBody @Validated BasepriceAirportDTO request){
        ResponseDTO result = basepriceAirportService.update(id, request);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping ({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deletedBasePriceAirport(@PathVariable UUID id){
        ResponseDTO result = basepriceAirportService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

//    @GetMapping({"","/"})
//    public ResponseEntity<ResponseDTO> searchBasePriceAirport(
//            @RequestParam(defaultValue = "0") int pageNumber,
//            @RequestParam(defaultValue = "100") int pageSize,
//            @RequestParam(defaultValue = "") String sortBy,
//            @ModelAttribute("airplaneClass") String airplaneClass,
//            @ModelAttribute("airplanePrice") String airplanePrice
//    ){
//        Pageable pageable;
//        if (sortBy.isEmpty()){
//            pageable = PageRequest.of(pageNumber, pageSize);
//        }else {
//            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
//        }
//        ResponseDTO result = basepriceAirportService.searchAll(airplaneClass, airplanePrice, pageable);
//        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
//    }
}
