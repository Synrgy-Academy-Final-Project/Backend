package com.example.finalProject.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Transaction;
import com.example.finalProject.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.finalProject.dto.TicketEntityDTO;
import com.example.finalProject.entity.Promotion;
import com.example.finalProject.entity.Ticket;
import com.example.finalProject.repository.TicketRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;

@Service
public class TicketImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public ResponseDTO searchAll(String seat, String gate, Pageable pageable) {
        String updatedSeat = generalFunction.createLikeQuery(seat);
        String updateGate = generalFunction.createLikeQuery(gate);

        return response.suksesDTO(ticketRepository.searchAll(updatedSeat, updateGate, pageable));
    }

    public ResponseDTO save(TicketEntityDTO ticket) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            Ticket convertToticket = modelMapper.map(ticket, Ticket.class);

            Optional<Transaction> checkTransactionData = transactionRepository.findById(ticket.getTransactionId());
            if(checkTransactionData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            convertToticket.setTransaction(checkTransactionData.get());

            Ticket result = ticketRepository.save(convertToticket);

            return response.suksesDTO(result);
        } catch (Exception e) {
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Ticket> checkData = ticketRepository.findById(id);
        if (checkData.isEmpty()) {
            return response.errorDTO(404, Config.DATA_NOT_FOUND);
        } else {
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, TicketEntityDTO ticket) {
        try {
            Optional<Ticket> checkData = ticketRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Ticket updateTicket = checkData.get();

            if (ticket.getTransactionId() != null) {
                Optional<Transaction> checkTransactionData = transactionRepository.findById(ticket.getTransactionId());
                if(checkTransactionData.isEmpty()){
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                updateTicket.setTransaction(checkTransactionData.get());
            }
            if (ticket.getGate() != null) {
                updateTicket.setGate(ticket.getGate());
            }
            if (ticket.getSeat() != null) {
                updateTicket.setSeat(ticket.getSeat());
            }

            Ticket saveTicket = ticketRepository.save(updateTicket);

            return response.suksesDTO(saveTicket);
        } catch (Exception e) {
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try {
            Optional<Ticket> checkData = ticketRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            Ticket deletedTicket = checkData.get();
            deletedTicket.setDeletedDate(new Date());
            return response.suksesDTO(ticketRepository.save(deletedTicket));
        } catch (Exception e) {
            return response.errorDTO(404, e.getMessage());
        }
    }
}
