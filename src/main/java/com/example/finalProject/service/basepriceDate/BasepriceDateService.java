package com.example.finalProject.service.basepriceDate;

import com.example.finalProject.dto.BasepriceDateDTO;
import com.example.finalProject.dto.ResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BasepriceDateService {
    @Transactional
    ResponseDTO save(BasepriceDateDTO request);

    ResponseDTO findById(UUID id);

    @Transactional
    ResponseDTO update(UUID id, BasepriceDateDTO request);

    @Transactional
    ResponseDTO delete(UUID id);

//    ResponseDTO searchAll(String dateFrom1, String dateFrom2, String dateTo1, String dateTo2, Integer priceDown, Integer priceUp, String type, Pageable pageable);

    ResponseDTO searchAll(String dateFrom1, String dateFrom2, String dateTo1, String dateTo2, String priceDown, String priceUp, String type, Pageable pageable);
}
