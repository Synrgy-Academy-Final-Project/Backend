package com.example.finalProject.controller;

import com.example.finalProject.dto.request.user.*;
import com.example.finalProject.dto.response.user.*;
import com.example.finalProject.service.user.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ){
        JwtResponseRegister register = authenticationServiceImpl.register(request);
        return ResponseHandler.generateResponse("success", register, null, HttpStatus.OK);
    }

    @PutMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(
            @RequestParam String email,
            @RequestBody VerifyAccountRequest verifyAccountRequest
            ) {
        JwtResponseRegister verify = authenticationServiceImpl.verifyAccount(email, verifyAccountRequest.getOtp());
        return ResponseHandler.generateResponse("success", verify, null, HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestParam String email) {
        RegenerateOtpResponse regenerateOtpResponse = authenticationServiceImpl.regenerateOtp(email);
        return ResponseHandler.generateResponse("success", regenerateOtpResponse, null, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ){
        JwtResponseLogin login = authenticationServiceImpl.login(request);
        return ResponseHandler.generateResponse("success", login, null, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ){
        JwtResponseForgotPassword forgotPassword = authenticationServiceImpl.forgotPassword(request);
        return ResponseHandler.generateResponse("success", forgotPassword, null, HttpStatus.OK);
    }

    @PutMapping("/verify-account-forgot")
    public ResponseEntity<?> verifyAccountForgot(@RequestParam String email,
                                                 @RequestBody VerifyAccountRequest verifyAccountRequest) {
        JwtResponseVerifyForgot jwtResponseVerifyForgot = authenticationServiceImpl.verifyAccountPassword(email, verifyAccountRequest.getOtp());
        return ResponseHandler.generateResponse("success", jwtResponseVerifyForgot, null, HttpStatus.OK);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            @RequestParam String email
    ) {
        JwtResponseVerifyForgot change = authenticationServiceImpl.changePassword(request, email);
        return ResponseHandler.generateResponse("success", change, null, HttpStatus.OK);
    }
}
