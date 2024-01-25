package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Data
@Where(clause = "deleted_date is null")
public class AirplaneService extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    @JsonIgnore
    @ToString.Exclude
    @OneToOne
    private AirplaneClass airplaneClass;

    @PositiveOrZero
    @NotNull
    int baggage;

    @PositiveOrZero
    @NotNull
    int cabinBaggage;

    @NotNull
    boolean meals;

    @NotNull
    boolean travelInsurance;

    @NotNull
    boolean inflightEntertainment;

    @NotNull
    boolean electricSocket;

    @NotNull
    boolean wifi;

    @NotNull
    boolean reschedule;

    @NotNull
    @PositiveOrZero
    int refund;
}
