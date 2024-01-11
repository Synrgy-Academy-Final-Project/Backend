package com.example.finalProject.service.user;


import com.example.finalProject.dto.request.user.*;
import com.example.finalProject.dto.response.user.*;
import com.example.finalProject.exception.*;
import com.example.finalProject.model.user.ERole;
import com.example.finalProject.model.user.Role;
import com.example.finalProject.model.user.User;

import java.util.Map;

public interface AuhenticationService{
    Map register(RegisterRequest request) throws NullRequestException, UserExistException;

    Map verifyAccount(String email, String otp) throws NullRequestException, UserNotVerifiedException, WrongOtpException, UserNotFoundException;

    Map regenerateOtp(String email) throws UserNotFoundException;

    Map login(LoginRequest request) throws NullRequestException, BadCredentials, UserNotVerifiedException, UserNotFoundException;
    Role addRole(ERole role);
    User getIdUser(String name) throws UserNotFoundException;

    Map changePassword(ChangePasswordAndroidRequest request, String email) throws NullRequestException, WrongOtpException, PasswordNotSameException, UserNotFoundException;

    Map forgotPassword(ForgotPasswordRequest request) throws UserNotFoundException;

    Map forgotPasswordWeb(ForgotPasswordRequest request) throws UserNotFoundException;

    Map verifyAccountPassword(String email, String otp) throws NullRequestException, WrongOtpException, UserNotFoundException;

    Map changePasswordWeb(String email, String token, ChangePasswordRequest request) throws PasswordNotSameException, UserNotVerifiedException, WrongOtpException, NullRequestException, UserNotFoundException;
}
