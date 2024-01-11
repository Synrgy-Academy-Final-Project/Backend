package com.example.finalProject.repository;

import com.example.finalProject.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "select * from transactions\n" +
            "where code ilike ?1 and name ilike ?2 and deleted_date is null",
            nativeQuery = true)
    public Page<Transaction> searchAll(String code, String name, Pageable pageable);
}
