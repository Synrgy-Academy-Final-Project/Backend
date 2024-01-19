package com.example.finalProject.service.airplaneFlightTime;

import com.example.finalProject.dto.AirplaneFlightTimeDTO;
import com.example.finalProject.dto.ResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AirplaneFlightTimeService {
    @Transactional
    ResponseDTO save(AirplaneFlightTimeDTO request);

    ResponseDTO findById(UUID id);

    @Transactional
    ResponseDTO update(UUID id, AirplaneFlightTimeDTO request);

    @Transactional
    ResponseDTO delete(UUID id);

    ResponseDTO searchAll(String airplaneClass, String airplanePrice, Pageable pageable);
}
