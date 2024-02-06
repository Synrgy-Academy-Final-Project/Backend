package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "transaction_airplane_additional_service")
@Where(clause = "deleted_date is null")
public class TransactionAirplaneAdditionalService extends AbstractDate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    @ToString.Exclude
    private Transaction transaction;

    @JsonIgnore
    @OneToOne
    private Passenger passenger;

    @PositiveOrZero
    @NotNull
    private Integer quantity;

    @PositiveOrZero
    @NotNull
    private Integer price;

    public TransactionAirplaneAdditionalService(Transaction transaction, Passenger passenger, Integer quantity, Integer price) {
        this.transaction = transaction;
        this.passenger = passenger;
        this.quantity = quantity;
        this.price = price;
    }
}
