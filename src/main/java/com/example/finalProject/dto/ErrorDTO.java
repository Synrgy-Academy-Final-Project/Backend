package com.example.finalProject.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class ErrorDTO {
    private HttpStatus code;

    private List<String> message;
}
