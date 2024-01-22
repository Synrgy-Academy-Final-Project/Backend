package com.example.finalProject.dto;

import com.example.finalProject.model.user.UserDetails;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TransactionEntityDTO {
    UUID id;

    @NotNull
    UUID userId;

    @NotNull
    UUID paymentId;

    @NotNull
    UUID flight1Id;

    UUID flight2Id;

    UUID promotionId;

    @NotNull
    List<UserDetails> userDetails;

    @NotNull
    @Positive
    Integer totalSeat;

    int totalPrice;
}
