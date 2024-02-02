package com.example.finalProject.repository;

import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
}
