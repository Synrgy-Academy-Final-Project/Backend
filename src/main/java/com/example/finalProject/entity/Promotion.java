package com.example.finalProject.entity;

import java.sql.Date;
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
    String discount;

    @NotNull
    String terms;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;
}
