package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class PromotionEntityDTO {

    private UUID id;

    @NotNull
    String title;

    @NotNull
    String description;

    @NotNull
    String code;

    @NotNull
    Integer discount;

    @NotNull
    String terms;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;
}