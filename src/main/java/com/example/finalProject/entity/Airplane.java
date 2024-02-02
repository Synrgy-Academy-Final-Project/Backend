package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "airplanes")
@Where(clause = "deleted_date is null")
public class Airplane extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotBlank
    String name;

    @NotBlank
    String code;

    @NotNull
    @Positive
    private Integer airplanePrice;


    @JsonIgnore
    @ManyToOne
    @JoinColumn
    @NotNull
    @ToString.Exclude
    private Company company;

//    @JsonIgnore
//    @ToString.Exclude
//    @OneToMany(mappedBy = "airplane")
//    List<Flight> flight;

    @OneToMany(mappedBy = "airplane")
    List<AirplaneClass> airplaneClass;

    @OneToMany(mappedBy = "airplane")
    List<AirplaneFlightTime> airplaneFlightTimes;

    @OneToMany(mappedBy = "airplane")
    List<AirplaneAdditionalService> airplaneAdditionalService;
}
