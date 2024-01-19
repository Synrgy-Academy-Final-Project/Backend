package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "basepriceAirports")
@Where(clause = "deleted_date is null")
public class BasePriceAirport extends AbstractDate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn
    private Airport fromAirport;
    private String departureCode;

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn
    private Airport toAirport;
    private String arrivalCode;

    private Integer duration;

    @NotNull
    @Positive
    private Integer airportPrice;

}
