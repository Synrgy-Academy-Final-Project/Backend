package com.example.finalProject.exception;

import com.example.finalProject.model.user.User;

public class UserExistException extends Exception{
    public UserExistException(){
        super("Email already used");
    }
}
