package com.example.finalProject.controller;

import java.util.Map;
import java.util.UUID;

import com.example.finalProject.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.finalProject.dto.PaymentEntityDTO;
import com.example.finalProject.service.PaymentImpl;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/payments")
@Slf4j
public class PaymentController {
    @Autowired
    PaymentImpl paymentImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> searchPayment(@RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "100") int pageSize,
                                                     @RequestParam(defaultValue = "") String sortBy,
                                                     @ModelAttribute("accountNumber") String accountNumber,
                                                     @ModelAttribute("bankName") String bankName) {
        Pageable pageable;
        if (sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNumber, pageSize);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        return new ResponseEntity<>(paymentImpl.searchAll(accountNumber, bankName, pageable), HttpStatus.OK);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> addPayment(@RequestBody @Validated PaymentEntityDTO payment) {
        return new ResponseEntity<>(paymentImpl.save(payment), HttpStatus.OK);
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> findPayment(@PathVariable UUID id) {
        return new ResponseEntity<>(paymentImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> updatePayment(@PathVariable UUID id,
            @RequestBody PaymentEntityDTO payment) {
        return new ResponseEntity<>(paymentImpl.update(id, payment), HttpStatus.OK);
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> deletePayment(@PathVariable UUID id) {

        return new ResponseEntity<>(paymentImpl.delete(id), HttpStatus.OK);
    }
}
