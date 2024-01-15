package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class PromotionEntityDTO {

    private UUID id;

    @NotBlank
    String title;

    @NotBlank
    String description;

    @NotBlank
    String code;

    @NotNull
    @Positive
    Integer discount;

    @NotBlank
    String terms;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;
}