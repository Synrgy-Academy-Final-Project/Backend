package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.util.UUID;
import lombok.Data;

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
    String discount;

    @NotNull
    String terms;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;
}