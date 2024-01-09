package com.example.finalProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight extends AbstractDate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Date departureDate;
    private Date arrivalDate;
    private Integer capacity;
    private String airplaneClass;
    private Integer price;

    @ManyToMany(mappedBy = "flights")
    private List<Transaction> transactions;
}
