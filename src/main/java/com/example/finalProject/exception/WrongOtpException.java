package com.example.finalProject.exception;

public class WrongOtpException extends Exception{
    public WrongOtpException(){
        super("Incorrect OTP");
    }
}
