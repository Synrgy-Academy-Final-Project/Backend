package com.example.finalProject.service;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.TicketEntityDTO;
import com.example.finalProject.entity.Ticket;
import com.example.finalProject.entity.Transaction;
import com.example.finalProject.repository.TicketRepository;
import com.example.finalProject.repository.TransactionRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketImpl {
    @Autowired
    Response response;
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
                return response.dataNotFound("Transaction");
            }
            convertToticket.setTransaction(checkTransactionData.get());

            Ticket result = ticketRepository.save(convertToticket);

            return response.suksesDTO(result);
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Ticket> checkData = ticketRepository.findById(id);
        if (checkData.isEmpty()) {
            return response.dataNotFound("Ticket");
        } else {
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, TicketEntityDTO ticket) {
        try {
            Optional<Ticket> checkData = ticketRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.dataNotFound("Ticket");
            }

            Ticket updateTicket = checkData.get();

            if (ticket.getTransactionId() != null) {
                Optional<Transaction> checkTransactionData = transactionRepository.findById(ticket.getTransactionId());
                if(checkTransactionData.isEmpty()){
                    return response.dataNotFound("Transaction");
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
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try {
            Optional<Ticket> checkData = ticketRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.dataNotFound("Ticket");
            }
            Ticket deletedTicket = checkData.get();
            deletedTicket.setDeletedDate(new Date());
            return response.suksesDTO(ticketRepository.save(deletedTicket));
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }
}
