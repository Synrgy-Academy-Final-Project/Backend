package com.example.finalProject.repository;

import com.example.finalProject.dto.ETicketDTO;
import com.example.finalProject.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "select * from transactions\n" +
            "where code ilike ?1 and name ilike ?2 and deleted_date is null",
            nativeQuery = true)
    public Page<Transaction> searchAll(String code, String name, Pageable pageable);

    @Query("select new com.example.finalProject.dto.ETicketDTO(u.fullName, a3.name, a3.code, " +
            "f1.airplaneClass, f1.departureDate," +
            "a1.name, a1.code, a1.city, a1.country, " +
            "f1.arrivalDate, a2.name, a2.code, a2.city, a2.country," +
            "t2.id, t2.gate, t2.seat) from User u " +
            "JOIN u.transaction t JOIN t.flight1 f1 JOIN f1.fromAirport a1 JOIN f1.toAirport a2 join f1.airplane a3 " +
            "join a3.company c join t.ticket t2 where u.id=:userId")
    List<ETicketDTO> getAllUserTransaction(@Param("userId") UUID userId);
}
