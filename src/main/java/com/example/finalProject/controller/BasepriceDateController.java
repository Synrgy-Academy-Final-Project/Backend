package com.example.finalProject.controller;

import com.example.finalProject.dto.BasepriceDateDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.basepriceDate.BasepriceDateServiceImpl;
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
@RequestMapping("/basepricedate")
@Slf4j
public class BasepriceDateController {
    private final BasepriceDateServiceImpl basepriceDateService;

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addBasepriceDate(@RequestBody @Validated BasepriceDateDTO request){
        ResponseDTO save = basepriceDateService.save(request);
        return new ResponseEntity<>(save, HttpStatus.valueOf(save.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findBasepriceDate(@PathVariable UUID id){
        ResponseDTO result = basepriceDateService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateBasepriceDate(@PathVariable UUID id, @RequestBody @Validated BasepriceDateDTO request){
        ResponseDTO result = basepriceDateService.update(id, request);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping ({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deletedBasepriceDate(@PathVariable UUID id){
        ResponseDTO result = basepriceDateService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"","/"})
    public ResponseEntity<ResponseDTO> searchBasepriceDate(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "100") int pageSize,
            @RequestParam(defaultValue = "") String sortBy,
            @ModelAttribute("dateFrom1") String dateFrom1,
            @ModelAttribute("dateFrom2") String dateFrom2,
            @ModelAttribute("dateTo1") String dateTo1,
            @ModelAttribute("dateTo2") String dateTo2,
            @ModelAttribute("priceDown") String priceDown,
            @ModelAttribute("priceUp") String priceUp,
            @ModelAttribute("type") String type
    ){
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = basepriceDateService.searchAll(dateFrom1, dateFrom2, dateTo1, dateTo2, priceDown, priceUp, type, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
