package com.example.finalProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;

@Data
public class BasepriceDateDTO {
    @NotNull
    private Date dateTime;
    @NotBlank
    private String type;
    @NotNull
    @Positive
    private Integer datePrice;
}
