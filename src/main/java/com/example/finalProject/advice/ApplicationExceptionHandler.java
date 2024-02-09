package com.example.finalProject.advice;

import com.example.finalProject.exception.*;
import com.example.finalProject.dto.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message",ex.getMessage());
        errorMap.put("status", HttpStatus.BAD_REQUEST.value());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullRequestException.class)
    public Map handleNullParameterException(NullRequestException ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message",ex.getMessage());
        errorMap.put("status", HttpStatus.BAD_REQUEST.value());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotFoundException.class)
    public Map handleUserNotFoundException(UserNotFoundException ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message", ex.getMessage());
        errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserExistException.class)
    public Map handleUserExistException(UserExistException ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message", ex.getMessage());
        errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentials.class)
    public Map handleBadCredentials(BadCredentials ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message",ex.getMessage());
        errorMap.put("status", HttpStatus.UNAUTHORIZED.value());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserNotVerifiedException.class)
    public Map handleUserNotVerified(UserNotVerifiedException ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message",ex.getMessage());
        errorMap.put("status", HttpStatus.FORBIDDEN.value());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(WrongOtpException.class)
    public Map handleWrongOtpException(WrongOtpException ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message",ex.getMessage());
        errorMap.put("status", HttpStatus.UNAUTHORIZED.value());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotSameException.class)
    public Map handlePasswordNotSameException(PasswordNotSameException ex) {
        Map errorMap = new HashMap<>();
        errorMap.put("message",ex.getMessage());
        errorMap.put("status", HttpStatus.BAD_REQUEST.value());
        return errorMap;
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<String> handleHttpMessageNotWritableException(HttpMessageNotWritableException ex) {
        // Log the exception or perform any necessary error handling

        // Return an error response to the client
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error writing response: " + ex.getMessage());
    }
}
