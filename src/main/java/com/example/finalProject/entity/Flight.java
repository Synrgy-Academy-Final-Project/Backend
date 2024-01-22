package com.example.finalProject.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "flights")
@Where(clause = "deleted_date is null")
public class Flight extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn
    Airplane airplane;

    @NotNull
    Date departureDate;

    @NotNull
    Date arrivalDate;

    @NotNull
    @PositiveOrZero
    int capacity;

    @NotBlank
    String airplaneClass;

    @NotNull
    @ManyToOne
    @JoinColumn
    Airport fromAirport;

    @NotNull
    @ManyToOne
    @JoinColumn
    Airport toAirport;

    @NotNull
    @Positive
    int price;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "flight1")
    List<Transaction> transaction1;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "flight2")
    List<Transaction> transaction2;

}
