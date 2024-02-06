package com.example.finalProject.repository;

import com.example.finalProject.entity.Passenger;
import com.example.finalProject.entity.Payment;
import com.example.finalProject.entity.SavedPassenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SavedPassengerRepository extends JpaRepository<SavedPassenger, UUID> {
    @Query(value = "select * from saved_passenger\n" +
            "where user_id = ?1 and user_details_id = ?2", nativeQuery = true)
    public SavedPassenger findByUserAndUserDetail(UUID userId, UUID userDetailId);
}
