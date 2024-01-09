package com.example.finalProject.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

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

    @ManyToOne
    @JoinColumn
    Transaction transaction;
}
