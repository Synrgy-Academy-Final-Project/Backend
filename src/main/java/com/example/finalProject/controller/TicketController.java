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

import com.example.finalProject.dto.TicketEntityDTO;
import com.example.finalProject.service.TicketImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/tickets")
@Slf4j
public class TicketController {
    @Autowired
    TicketImpl ticketImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> searchTicket(@RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "100") int pageSize,
                                                    @RequestParam(defaultValue = "") String sortBy,
                                                    @ModelAttribute("seat") String seat,
                                                    @ModelAttribute("gate") String gate) {
        Pageable pageable;
        if (sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNumber, pageSize);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        return new ResponseEntity<>(ticketImpl.searchAll(seat, gate, pageable), HttpStatus.OK);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> addTicket(@RequestBody @Validated TicketEntityDTO Ticket) {
        return new ResponseEntity<>(ticketImpl.save(Ticket), HttpStatus.OK);
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> findTicket(@PathVariable UUID id) {
        return new ResponseEntity<>(ticketImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> updateTicket(@PathVariable UUID id,
            @RequestBody TicketEntityDTO Ticket) {
        return new ResponseEntity<>(ticketImpl.update(id, Ticket), HttpStatus.OK);
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> deleteTicket(@PathVariable UUID id) {

        return new ResponseEntity<>(ticketImpl.delete(id), HttpStatus.OK);
    }
}
