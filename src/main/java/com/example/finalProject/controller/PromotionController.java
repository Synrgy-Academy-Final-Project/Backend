package com.example.finalProject.controller;

import com.example.finalProject.dto.PromotionEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.PromotionImpl;

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
@RequestMapping("/promotions")
@Slf4j
public class PromotionController {
    @Autowired
    PromotionImpl promotionImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> searchPromotion(@RequestParam(defaultValue = "0") int pageNumber,
                                                       @RequestParam(defaultValue = "100") int pageSize,
                                                       @RequestParam(defaultValue = "") String sortBy,
                                                       @ModelAttribute("title") String title,
                                                       @ModelAttribute("code") String code) {
        Pageable pageable;
        if (sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNumber, pageSize);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = promotionImpl.searchAll(code, title, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> addPromotion(@RequestBody @Validated PromotionEntityDTO Promotion) {
        ResponseDTO result = promotionImpl.save(Promotion);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> findPromotion(@PathVariable UUID id) {
        ResponseDTO result = promotionImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> updatePromotion(@PathVariable UUID id,
            @RequestBody PromotionEntityDTO Promotion) {
        ResponseDTO result = promotionImpl.update(id, Promotion);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> deletePromotion(@PathVariable UUID id) {
        ResponseDTO result = promotionImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
