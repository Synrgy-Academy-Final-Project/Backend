package com.example.finalProject.entity;

import com.example.finalProject.model.user.User;
import com.example.finalProject.model.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;


import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "order_code")
        })
@Where(clause = "deleted_date is null")
public class Transaction extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn
    User user;

    @NotBlank
    String companyName;

    @NotBlank
    String url;

    UUID airplaneId;

    @NotBlank
    String airplaneName;

    @NotBlank
    String airplaneCode;

    //    @NotNull
//    @ManyToOne
//    @JoinColumn
//    AirplaneClass airplaneClasss;
    UUID airplaneClassId;

    @NotBlank
    String airplaneClass;

    //    @NotNull
//    @ManyToOne
//    @JoinColumn
//    AirplaneFlightTime airplaneTimeFLight;
    UUID airplaneTimeFlightId;

    @NotBlank
    String departureCode;

    LocalDate departureDate;

    Time departureTime;

    @NotBlank
    String arrivalCode;

    LocalDate arrivalDate;

    Time arrivalTime;

    @NotNull
    @Positive
    Integer totalSeat;

    @NotNull
    @Positive
    Integer priceFlight;

    Integer seatMature;

    Integer totalMatureTransaction;

    Integer seatBaby;

    Integer totalBabyTransaction;

    @NotNull
    @Positive
    Integer totalPrice;

    @NotBlank
    String orderCode;

    @ManyToOne
    @JoinColumn
    Promotion promotion;

    Integer discount;

    Integer totalDiscount;

    Integer taxAdmin;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "transaction")
    Payment payment;

    @OneToMany(mappedBy = "transaction")
    List<Passenger> passenger;

    @JsonIgnore
    @OneToMany(mappedBy = "transaction")
    List<Ticket> ticket;

    @JsonIgnore
    @OneToMany(mappedBy = "transaction")
    List<TransactionAirplaneAdditionalService> transactionAirplaneAdditionalServices;
}
