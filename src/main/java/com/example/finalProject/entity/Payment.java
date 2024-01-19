package com.example.finalProject.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Where;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "payments")
@Where(clause = "deleted_date is null")
public class Payment extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    LocalDateTime transaction_time;

    String transaction_status;

    String status_message;

    int status_code;

    String signature_key;

    String payment_type;

    String merchant_id;

    double grossAmount;

    String fraud_status;

    String currency;

    @JsonIgnore
    @OneToOne
    Transaction transaction;
}
