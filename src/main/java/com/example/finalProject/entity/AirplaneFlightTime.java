package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.Where;

import java.sql.Time;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "airplane_flight_times")
@Where(clause = "deleted_date is null")
public class AirplaneFlightTime extends AbstractDate{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    @NotNull
    private Airplane airplane;

    @NotBlank
    private Time flightTime;

    @NotNull
    @Positive
    private Integer airplaneFlightTimePrice;
}
