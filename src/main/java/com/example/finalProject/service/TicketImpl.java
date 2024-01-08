package com.example.finalProject.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public Page<Ticket> searchAll(String seat, String gate, Pageable pageable) {
        String updatedSeat = generalFunction.createLikeQuery(seat);
        String updateGate = generalFunction.createLikeQuery(gate);
        return ticketRepository.searchAll(updatedSeat, updateGate, pageable);
    }

    public Map<String, Object> save(TicketEntityDTO ticket) {
        Map<String, Object> map = new HashMap<>();

        try {
            ModelMapper modelMapper = new ModelMapper();
            Ticket convertToticket = modelMapper.map(ticket, Ticket.class);
            Ticket result = ticketRepository.save(convertToticket);

            map = response.sukses(result);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> findById(UUID id) {
        Map<String, Object> map;

        Optional<Ticket> checkData = ticketRepository.findById(id);
        if (checkData.isEmpty()) {
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        } else {
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map<String, Object> update(UUID id, TicketEntityDTO ticket) {
        Map<String, Object> map;
        try {
            Optional<Ticket> checkData = ticketRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Ticket updateTicket = checkData.get();

            if (ticket.getTransactionId() != null) {
                updateTicket.setTransactionId(ticket.getTransactionId());
            }
            if (ticket.getGate() != null) {
                updateTicket.setGate(ticket.getGate());
            }
            if (ticket.getSeat() != null) {
                updateTicket.setSeat(ticket.getSeat());
            }

            Ticket saveTicket = ticketRepository.save(updateTicket);

            map = response.sukses(saveTicket);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> delete(UUID id) {
        Map<String, Object> map;
        try {
            Optional<Ticket> checkData = ticketRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            Ticket deletedTicket = checkData.get();
            deletedTicket.setDeletedDate(new Date());
            map = response.sukses(ticketRepository.save(deletedTicket));
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
