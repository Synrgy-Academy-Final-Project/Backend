package com.example.finalProject.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalProject.dto.PaymentEntityDTO;
import com.example.finalProject.service.PaymentImpl;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payments")
@Slf4j
public class PaymentController {
    @Autowired
    PaymentImpl paymentImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> searchPayment(@RequestParam(defaultValue = "0") int pageNumber,
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
    public ResponseEntity<Map<String, Object>> addPayment(@RequestBody @Validated PaymentEntityDTO payment) {
        return new ResponseEntity<>(paymentImpl.save(payment), HttpStatus.OK);
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> findPayment(@PathVariable UUID id) {
        return new ResponseEntity<>(paymentImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> updatePayment(@PathVariable UUID id,
            @RequestBody PaymentEntityDTO payment) {
        return new ResponseEntity<>(paymentImpl.update(id, payment), HttpStatus.OK);
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> deletePayment(@PathVariable UUID id) {

        return new ResponseEntity<>(paymentImpl.delete(id), HttpStatus.OK);
    }
}
