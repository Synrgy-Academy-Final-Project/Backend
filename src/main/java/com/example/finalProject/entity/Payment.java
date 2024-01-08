package com.example.finalProject.entity;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @NotNull
    String accountName;

    @NotNull
    String accountNumber;

    @NotNull
    String bankName;
}
