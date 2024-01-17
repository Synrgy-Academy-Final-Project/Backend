package com.example.finalProject.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.finalProject.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Query(value = "select * from payments " +
            "where (lower(account_number) like lower(concat('%', ?1, '%'))" +
            "or lower(bank_name) like lower(concat('%', ?2, '%'))) " +
            "and deleted_date is null", nativeQuery = true)
    public Page<Payment> searchAll(String accountNumber, String bankName, Pageable pageable);

    @Modifying
    @Query(value = "delete from payments\n" +
            "where transaction_id = ?1", nativeQuery = true)
    public void deleteByTransactionId(UUID id);
}
