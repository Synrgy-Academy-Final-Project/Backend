package com.example.finalProject.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class DataMatureDTO {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Integer qty;
    private Integer price;

    public DataMatureDTO(String firstName, String lastName, LocalDate dateOfBirth, Integer qty, Integer price) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.qty = qty;
        this.price = price;
    }
}
