package com.example.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
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

    @NotBlank
    String name;

    @NotBlank
    String url;

    @OneToMany(mappedBy = "company")
    List<Airplane> airplane;
}
