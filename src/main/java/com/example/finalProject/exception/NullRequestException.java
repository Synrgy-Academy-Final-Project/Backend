package com.example.finalProject.exception;

public class NullRequestException extends Exception{
    public NullRequestException() {
        super("Field cannot be null");
    }
}
