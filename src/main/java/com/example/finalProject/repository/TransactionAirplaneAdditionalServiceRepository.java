package com.example.finalProject.repository;

import com.example.finalProject.entity.TransactionAirplaneAdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionAirplaneAdditionalServiceRepository extends JpaRepository<TransactionAirplaneAdditionalService,UUID> {
}
