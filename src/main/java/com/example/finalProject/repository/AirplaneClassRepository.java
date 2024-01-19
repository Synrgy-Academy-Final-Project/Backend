package com.example.finalProject.repository;

import com.example.finalProject.entity.AirplaneClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirplaneClassRepository extends JpaRepository<AirplaneClass, UUID> {
}
