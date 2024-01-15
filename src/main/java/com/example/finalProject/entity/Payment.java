package com.example.finalProject.entity;

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

    @NotBlank
    String accountName;

    @NotBlank
    String accountNumber;

    @NotBlank
    String bankName;

    @JsonIgnore
    @OneToMany(mappedBy = "payment")
    List<Transaction> transaction;
}
