package com.example.finalProject.advice;

import com.example.finalProject.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        errorMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
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
}
