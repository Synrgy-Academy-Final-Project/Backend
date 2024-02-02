package com.example.finalProject.repository;

import com.example.finalProject.entity.AirplaneAdditionalService;
import com.example.finalProject.entity.AirplaneService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirplaneAdditionalServiceRepository extends JpaRepository<AirplaneAdditionalService, UUID> {
}
