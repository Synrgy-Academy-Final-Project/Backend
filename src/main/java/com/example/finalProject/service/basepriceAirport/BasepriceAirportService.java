package com.example.finalProject.service.basepriceAirport;

import com.example.finalProject.dto.BasepriceAirportDTO;
import com.example.finalProject.dto.ResponseDTO;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface BasepriceAirportService {
    @Transactional
    ResponseDTO save(BasepriceAirportDTO request);

    ResponseDTO findById(UUID id);

    @Transactional
    ResponseDTO update(UUID id, BasepriceAirportDTO request);

    @Transactional
    ResponseDTO delete(UUID id);
}
