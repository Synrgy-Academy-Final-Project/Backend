package com.example.finalProject.repository;

import com.example.finalProject.entity.AirplaneFlightTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirplaneFlightTimeRepository extends JpaRepository<AirplaneFlightTime, UUID> {
}
