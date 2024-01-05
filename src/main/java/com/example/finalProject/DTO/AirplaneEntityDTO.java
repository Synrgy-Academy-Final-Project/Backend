package com.example.finalProject.DTO;

import com.example.finalProject.entity.Company;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AirplaneEntityDTO {
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    String code;
    @NotNull
    private UUID companyId;
}
