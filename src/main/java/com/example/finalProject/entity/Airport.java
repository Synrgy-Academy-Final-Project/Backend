package com.example.finalProject.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "airports")
@Where(clause = "deleted_date is null")
public class Airport extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    String name;

    @NotNull
    String code;

    @NotNull
    String city;

    @NotNull
    String country;

    @JsonIgnore
    @OneToMany(mappedBy = "fromAirport")
    List<Flight> startFlight;

    @JsonIgnore
    @OneToMany(mappedBy = "toAirport")
    List<Flight> endFlight;
}
