package com.example.finalProject.controller;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.TransactionEntityDTO;
import com.example.finalProject.service.TransactionImpl;
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
        return new ResponseEntity<>(transactionImpl.searchAll(pageable), HttpStatus.OK);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addTransaction(@RequestBody @Validated TransactionEntityDTO transaction){
        return new ResponseEntity<>(transactionImpl.save(transaction), HttpStatus.OK);
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findTransaction(@PathVariable UUID id){
        return new ResponseEntity<>(transactionImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateTransaction(@PathVariable UUID id, @RequestBody  TransactionEntityDTO transaction){
        return new ResponseEntity<>(transactionImpl.update(id, transaction), HttpStatus.OK);
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleteTransaction(@PathVariable UUID id){
        return new ResponseEntity<>(transactionImpl.delete(id), HttpStatus.OK);
    }
}
