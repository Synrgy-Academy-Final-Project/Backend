package com.example.finalProject.entity;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @NotNull
    String title;

    @NotNull
    String description;

    @NotNull
    String code;

    @NotNull
    int discount;

    @NotNull
    String terms;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "promotion")
    List<Transaction> transaction;
}
