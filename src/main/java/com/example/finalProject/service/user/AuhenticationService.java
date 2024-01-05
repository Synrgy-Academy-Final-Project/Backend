package com.example.finalProject.service.user;


import com.example.finalProject.dto.request.user.*;
import com.example.finalProject.dto.response.user.*;
import com.example.finalProject.model.user.ERole;
import com.example.finalProject.model.user.Role;
import com.example.finalProject.model.user.User;

import java.util.Set;

public interface AuhenticationService{
    JwtResponseRegister register(RegisterRequest request);

    JwtResponseRegister verifyAccount(String email, String otp);

    RegenerateOtpResponse regenerateOtp(String email);

    JwtResponseLogin login(LoginRequest request);
    Role addRole(ERole role);
    User getIdUser(String name);

    JwtResponseVerifyForgot changePassword(ChangePasswordRequest request, String email);

    JwtResponseForgotPassword forgotPassword(ForgotPasswordRequest request);

    JwtResponseVerifyForgot verifyAccountPassword(String email, String otp);
}
