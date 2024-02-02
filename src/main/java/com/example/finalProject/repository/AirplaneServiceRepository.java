package com.example.finalProject.repository;

import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.AirplaneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.UUID;

public interface AirplaneServiceRepository extends JpaRepository<AirplaneService, UUID> {

}
