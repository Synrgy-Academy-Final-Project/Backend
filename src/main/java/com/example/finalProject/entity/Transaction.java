package com.example.finalProject.entity;

import com.example.finalProject.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Where;

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

    @NotNull
    @ManyToOne
    @JoinColumn
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

    @NotNull
    int totalSeat;

    @NotNull
    int totalPrice;
}
