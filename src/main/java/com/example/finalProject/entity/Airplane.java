package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Data
@Table(name = "airplanes")
@Where(clause = "deleted_date is null")
public class Airplane extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    String name;
    @NotNull
    String code;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Company company;
}
