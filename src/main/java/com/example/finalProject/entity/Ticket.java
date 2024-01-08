package com.example.finalProject.entity;

import java.util.UUID;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "tickets")
@Where(clause = "deleted_date is null")
public class Ticket extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "seat")
    private String seat;

    @NotNull
    @Column(name = "gate")
    private String gate;

    @Column(name = "transaction_id")
    private UUID transactionId;
}
