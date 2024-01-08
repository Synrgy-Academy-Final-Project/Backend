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

import com.example.finalProject.dto.TicketEntityDTO;
import com.example.finalProject.service.TicketImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tickets")
@Slf4j
public class TicketController {
    @Autowired
    TicketImpl ticketImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> searchTicket(@RequestParam(defaultValue = "0") int pageNumber,
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
    public ResponseEntity<Map<String, Object>> addTicket(@RequestBody @Validated TicketEntityDTO Ticket) {
        return new ResponseEntity<>(ticketImpl.save(Ticket), HttpStatus.OK);
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> findTicket(@PathVariable UUID id) {
        return new ResponseEntity<>(ticketImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> updateTicket(@PathVariable UUID id,
            @RequestBody TicketEntityDTO Ticket) {
        return new ResponseEntity<>(ticketImpl.update(id, Ticket), HttpStatus.OK);
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<Map<String, Object>> deleteTicket(@PathVariable UUID id) {

        return new ResponseEntity<>(ticketImpl.delete(id), HttpStatus.OK);
    }
}
