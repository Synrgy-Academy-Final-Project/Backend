package com.example.finalProject.service.airplaneClass;


import com.example.finalProject.dto.AirplaneClassDTO;
import com.example.finalProject.dto.ResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AirplaneClassService {
    @Transactional
    ResponseDTO save(AirplaneClassDTO request);

    ResponseDTO findById(UUID id);

    @Transactional
    ResponseDTO update(UUID id, AirplaneClassDTO request);

    @Transactional
    ResponseDTO delete(UUID id);

    ResponseDTO searchAll(String airplaneClass, String airplanePrice, Pageable pageable);
}
