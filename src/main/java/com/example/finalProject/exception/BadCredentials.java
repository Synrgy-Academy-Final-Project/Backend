package com.example.finalProject.exception;

public class BadCredentials extends Exception{
    public BadCredentials(){
        super("email or password is wrong");
    }
}
