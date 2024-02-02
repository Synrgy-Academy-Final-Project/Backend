package com.example.finalProject.controller;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.TransactionEntityDTO;
import com.example.finalProject.service.TransactionImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {
    @Autowired
    TransactionImpl transactionImpl;

    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> searchTransaction(@RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "100") int pageSize,
                                                         @RequestParam(defaultValue = "") String sortBy){
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = transactionImpl.searchAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addTransaction(@RequestBody @Validated TransactionEntityDTO transaction) throws IOException {
        ResponseDTO result = transactionImpl.save(transaction);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"midtrans", "midtrans/"})
    public ResponseEntity<ResponseDTO> addAndCreateMidtrans(@RequestBody @Validated TransactionEntityDTO transaction) throws IOException, InterruptedException {
        ResponseDTO result = transactionImpl.createMidtransRequest(transaction);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

//    @GetMapping({"{id}", "{id}/"})
//    public ResponseEntity<ResponseDTO> findTransaction(@PathVariable UUID id){
//        ResponseDTO result = transactionImpl.findById(id);
//        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
//    }
//
//    @PutMapping({"{id}", "{id}/"})
//    public ResponseEntity<ResponseDTO> updateTransaction(@PathVariable UUID id, @RequestBody  TransactionEntityDTO transaction){
//        ResponseDTO result = transactionImpl.update(id, transaction);
//        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
//    }
//
//    @DeleteMapping({"{id}", "{id}/"})
//    public ResponseEntity<ResponseDTO> deleteTransaction(@PathVariable UUID id){
//        ResponseDTO result = transactionImpl.delete(id);
//        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
//    }
}
