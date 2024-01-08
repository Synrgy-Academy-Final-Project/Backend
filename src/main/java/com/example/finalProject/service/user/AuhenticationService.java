package com.example.finalProject.service.user;


import com.example.finalProject.dto.request.user.ChangePasswordRequest;
import com.example.finalProject.dto.request.user.ForgotPasswordRequest;
import com.example.finalProject.dto.request.user.LoginRequest;
import com.example.finalProject.dto.request.user.RegisterRequest;
import com.example.finalProject.dto.response.user.*;
import com.example.finalProject.model.user.ERole;
import com.example.finalProject.model.user.Role;
import com.example.finalProject.model.user.User;

import java.util.Map;

public interface AuhenticationService{
    Map register(RegisterRequest request);

    JwtResponseRegister verifyAccount(String email, String otp);

    RegenerateOtpResponse regenerateOtp(String email);

    Map login(LoginRequest request);
    Role addRole(ERole role);
    User getIdUser(String name);

    JwtResponseVerifyForgot changePassword(ChangePasswordRequest request, String email);

    JwtResponseForgotPassword forgotPassword(ForgotPasswordRequest request);

    JwtResponseForgotPassword forgotPasswordWeb(ForgotPasswordRequest request);

    TokenResponse verifyAccountPassword(String email, String otp);

    JwtResponseVerifyForgot changePasswordWeb(String email, String token, ChangePasswordRequest request);
}
