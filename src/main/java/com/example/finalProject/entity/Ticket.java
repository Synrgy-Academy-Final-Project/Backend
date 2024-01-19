package com.example.finalProject.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "seat")
    private String seat;

    @NotBlank
    @Column(name = "gate")
    private String gate;

    @NotNull
    @ManyToOne
    @JoinColumn
    Transaction transaction;
}
