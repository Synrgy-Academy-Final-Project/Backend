package com.example.finalProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ResponseDTO {
    @NotNull
    private int status;
    @NotNull
    String message;
    @NotNull
    Object data;

    public ResponseDTO(int status, String message){
        this.status = status;
        this.message = message;
    }
}
