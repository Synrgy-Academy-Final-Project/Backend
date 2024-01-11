package com.example.finalProject.advice;

import com.example.finalProject.dto.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleDBEntityNotFound(EntityNotFoundException ex, WebRequest webRequest) {
        ErrorDTO error = new ErrorDTO();
        error.setCode(HttpStatus.UNPROCESSABLE_ENTITY);
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        error.setMessage(message);
        return new ResponseEntity<>(error, error.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidArgument(MethodArgumentNotValidException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setCode(HttpStatus.BAD_REQUEST);
        List<String> message = new ArrayList<>();
        List<String> collect = ex.getBindingResult().getFieldErrors().stream().filter(Objects::nonNull)
                .map(m -> (m.getField() + " " + m.getDefaultMessage())).toList();
        message.addAll(collect);
        error.setMessage(message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
