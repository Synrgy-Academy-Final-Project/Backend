package com.example.finalProject;

import com.example.finalProject.dto.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleDBEntityNotFound(EntityNotFoundException ex, WebRequest webRequest) {
        System.out.println(ex);
        ErrorDTO error = new ErrorDTO();
        error.setCode(HttpStatus.UNPROCESSABLE_ENTITY);
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        error.setMessage(message);
        return new ResponseEntity<Object>(error, error.getCode());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        System.out.println(ex);
        ErrorDTO error = new ErrorDTO();
        error.setCode(HttpStatus.BAD_REQUEST);
        List<String> message = new ArrayList<String>();
        List<String> collect = ex.getBindingResult().getFieldErrors().stream().filter(Objects::nonNull)
                .map(m -> (m.getField() + " " + m.getDefaultMessage())).collect(Collectors.toList());
        message.addAll(collect);
        error.setMessage(message);
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
    }
}
