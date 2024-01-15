package com.example.finalProject.entity;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.Where;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "promotions")
@Where(clause = "deleted_date is null")
public class Promotion extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotBlank
    String title;

    @NotBlank
    String description;

    @NotBlank
    String code;

    @NotNull
    @Positive
    int discount;

    @NotBlank
    String terms;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "promotion")
    List<Transaction> transaction;
}
