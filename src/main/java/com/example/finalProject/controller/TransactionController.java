package com.example.finalProject.controller;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.TransactionEntityDTO;
import com.example.finalProject.exception.UserNotFoundException;
import com.example.finalProject.service.transaction.TransactionServiceImpl;
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
import java.security.Principal;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {
    @Autowired
    TransactionServiceImpl transactionImpl;

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
    public ResponseEntity<ResponseDTO> addTransaction(@RequestBody @Validated TransactionEntityDTO transaction, Principal principal) throws IOException, UserNotFoundException {
        ResponseDTO result = transactionImpl.save( principal, transaction);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"midtrans", "midtrans/"})
    public ResponseEntity<ResponseDTO> addAndCreateMidtrans(@RequestBody @Validated TransactionEntityDTO transaction, Principal principal ) throws IOException, InterruptedException, UserNotFoundException {
        ResponseDTO result = transactionImpl.createMidtransRequest(transaction, principal);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"history", "history/"})
    public ResponseEntity<ResponseDTO> transactionHistory(@RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "100") int pageSize,
                                                         @RequestParam(defaultValue = "") String sortBy,
                                                          Principal principal) throws UserNotFoundException {
        Pageable pageable;
        if (sortBy.isEmpty()){
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = transactionImpl.transactionHistory(principal, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
    @GetMapping({"history/detail/{orderId}", "history/detail/{orderId}/"})
    public ResponseEntity<ResponseDTO> transactionHistory(Principal principal,
                                                          @PathVariable UUID orderId) throws UserNotFoundException {
        ResponseDTO result = transactionImpl.transactionHistoryDetail(principal, orderId);
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
