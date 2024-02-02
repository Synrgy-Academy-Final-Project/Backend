package com.example.finalProject.controller;

import java.util.Map;
import java.util.UUID;

import com.example.finalProject.dto.MidtransResponseDTO;
import com.example.finalProject.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.finalProject.service.PaymentImpl;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
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
        ResponseDTO result = paymentImpl.searchAll(accountNumber, bankName, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

//    @PostMapping({ "", "/" })
//    public ResponseEntity<ResponseDTO> addPayment(@RequestBody @Validated PaymentEntityDTO payment) {
//        ResponseDTO result = paymentImpl.save(payment);
//        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
//    }

    @PostMapping({ "midtrans/handling", "midtrans/handling/" })
    public ResponseEntity<ResponseDTO> receiveMidtransPayment(@RequestBody @Validated MidtransResponseDTO midtransResponse) {
        ResponseDTO result = paymentImpl.saveMidtrans(midtransResponse);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({ "midtrans/unfinish", "midtrans/unfinish/" })
    public String unfinishMidtransPayment() {
        System.out.println("unfinish midtrans payment");
        return "hello";
    }

    @GetMapping({ "midtrans/finish", "midtrans/finish/" })
    public String finishMidtransPayment() {
        System.out.println("finish midtrans payment");
        return "hello";
    }

    @PostMapping({ "midtrans/finish", "midtrans/finish/" })
    public String postfinishMidtransPayment(@RequestBody @Validated MidtransResponseDTO midtransResponse) {
        System.out.println("post finish midtrans payment");
        System.out.println(midtransResponse);
        return "hello";
    }

    @GetMapping({ "midtrans/error", "midtrans/error/" })
    public String errorMidtransPayment() {
        System.out.println("error midtrans payment");
        return "hello";
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> findPayment(@PathVariable UUID id) {
        ResponseDTO result = paymentImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

//    @PutMapping({ "{id}", "{id}/" })
//    public ResponseEntity<ResponseDTO> updatePayment(@PathVariable UUID id,
//            @RequestBody PaymentEntityDTO payment) {
//        ResponseDTO result = paymentImpl.update(id, payment);
//        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
//    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> deletePayment(@PathVariable UUID id) {
        ResponseDTO result = paymentImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
