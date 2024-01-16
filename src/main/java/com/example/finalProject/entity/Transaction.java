package com.example.finalProject.entity;

import com.example.finalProject.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.Where;


import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions")
@Where(clause = "deleted_date is null")
public class Transaction extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn
    User user;

    @JsonIgnore
    @OneToOne(mappedBy = "transaction")
    Payment payment;

    @NotNull
    @ManyToOne
    @JoinColumn
    Flight flight1;

    @ManyToOne
    @JoinColumn
    Flight flight2;

    @ManyToOne
    @JoinColumn
    Promotion promotion;

    @JsonIgnore
    @OneToMany(mappedBy = "transaction")
    List<Ticket> ticket;

    @NotNull
    @Positive
    int totalSeat;

    @NotNull
    @Positive
    int totalPrice;

}
