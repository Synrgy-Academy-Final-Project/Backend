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
@CrossOrigin(origins = "*")
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
        ResponseDTO result = ticketImpl.searchAll(seat, gate, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> addTicket(@RequestBody @Validated TicketEntityDTO Ticket) {
        ResponseDTO result = ticketImpl.save(Ticket);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> findTicket(@PathVariable UUID id) {
        ResponseDTO result = ticketImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> updateTicket(@PathVariable UUID id,
            @RequestBody TicketEntityDTO Ticket) {
        ResponseDTO result = ticketImpl.update(id, Ticket);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> deleteTicket(@PathVariable UUID id) {
        ResponseDTO result = ticketImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
