package com.example.finalProject.entity;

import com.example.finalProject.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "airplane_classes")
@Where(clause = "deleted_date is null")
public class AirplaneClass extends AbstractDate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    @NotNull
    private Airplane airplane;

    @NotBlank
    private String airplaneClass;

    @NotBlank
    @Positive
    private Integer capacity;

    @NotNull
    @Positive
    private Integer airplaneClassPrice;

    @OneToOne(mappedBy = "airplaneClass")
    private AirplaneService airplaneService;

}
