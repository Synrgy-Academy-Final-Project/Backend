package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class AirplaneAdditionalService extends AbstractDate {
    public AirplaneAdditionalService(Airplane airplane, String type, int quantity, int price){
        this.airplane = airplane;
        this.type = type;
        this.quantity= quantity;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    @NotNull
    private Airplane airplane;

    @NotBlank
    String type;

    @PositiveOrZero
    int quantity;

    @NotNull
    @PositiveOrZero
    int price;
}
