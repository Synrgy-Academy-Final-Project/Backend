package com.example.finalProject.controller;

import com.example.finalProject.dto.request.user.*;
import com.example.finalProject.dto.response.user.*;
import com.example.finalProject.service.user.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<Map> register(
            @RequestBody RegisterRequest request
    ){
        return new ResponseEntity<>(authenticationServiceImpl.register(request), HttpStatus.OK);
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
    public ResponseEntity<Map> login(
            @RequestBody LoginRequest request
    ){
        return new ResponseEntity<>(authenticationServiceImpl.login(request), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ){
        JwtResponseForgotPassword forgotPassword = authenticationServiceImpl.forgotPassword(request);
        return ResponseHandler.generateResponse("success", forgotPassword, null, HttpStatus.OK);
    }

    @PostMapping("/forgot-password-web")
    public ResponseEntity<?> forgotPasswordWeb(
            @RequestBody ForgotPasswordRequest request
    ){
        JwtResponseForgotPassword forgotPassword = authenticationServiceImpl.forgotPasswordWeb(request);
        return ResponseHandler.generateResponse("success", forgotPassword, null, HttpStatus.OK);
    }


    @PutMapping("/verify-account-forgot")
    public ResponseEntity<?> verifyAccountForgot(@RequestParam String email,
                                                 @RequestBody VerifyAccountRequest verifyAccountRequest) {
        TokenResponse verifyAcount = authenticationServiceImpl.verifyAccountPassword(email, verifyAccountRequest.getOtp());
        return ResponseHandler.generateResponse("success", verifyAcount, null, HttpStatus.OK);
    }

    @PutMapping("/forgotpassword-web")
    public ResponseEntity<?> forgotPasswordWeb(@RequestParam String email,
                                                 @RequestParam String token,
                                               @RequestBody ChangePasswordRequest request) {
        JwtResponseVerifyForgot changePassword = authenticationServiceImpl.changePasswordWeb(email, token, request);
        return ResponseHandler.generateResponse("success", changePassword, null, HttpStatus.OK);
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
