package com.example.finalProject.service.transaction;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.TransactionEntityDTO;
import com.example.finalProject.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

public interface TransactionService {

    ResponseDTO searchAll(Pageable pageable);

    ResponseDTO save(Principal principal, TransactionEntityDTO request) throws IOException, UserNotFoundException;

    ResponseDTO transactionHistory(Principal principal, Pageable pageable) throws UserNotFoundException;

    ResponseDTO transactionHistoryDetail(Principal principal, UUID orderId) throws UserNotFoundException;
}
