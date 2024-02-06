package com.example.finalProject.entity;

import com.example.finalProject.model.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "passengers")
@Where(clause = "deleted_date is null")
public class Passenger extends AbstractDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    @ToString.Exclude
    Transaction transaction;

    @NotNull
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    @ToString.Exclude
    UserDetails userDetails;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "passenger")
    TransactionAirplaneAdditionalService transactionAirplaneAdditionalService;

    public Passenger(Transaction transaction, UserDetails userDetails){
        this.transaction = transaction;
        this.userDetails = userDetails;
    }
}
