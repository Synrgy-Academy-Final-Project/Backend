package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "companies")
@Where(clause = "deleted_date is null")
public class Company extends AbstractDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    String name;

    @OneToMany(mappedBy = "company")
    List<Airplane> airplane;
}
