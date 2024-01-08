package com.example.finalProject.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.finalProject.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    @Query(value = "SELECT * FROM tickets " +
            "WHERE (lower(seat) LIKE lower(concat('%', ?1 , '%')) " +
            "OR lower(gate) LIKE lower(concat('%', ?2 , '%'))) " +
            "AND deleted_date IS NULL", nativeQuery = true)
    Page<Ticket> searchAll(
            @Param("seat") String seat,
            @Param("gate") String gate,
            Pageable pageable);
}
