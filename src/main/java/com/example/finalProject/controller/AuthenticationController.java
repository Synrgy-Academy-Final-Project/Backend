package com.example.finalProject.controller;

import com.example.finalProject.dto.RefreshTokenRequestDTO;
import com.example.finalProject.dto.request.user.*;
import com.example.finalProject.entity.RefreshToken;
import com.example.finalProject.exception.*;
import com.example.finalProject.repository.RefreshTokenRepository;
import com.example.finalProject.security.service.JwtService;
import com.example.finalProject.security.service.UserService;
import com.example.finalProject.service.RefreshTokenServiceImpl;
import com.example.finalProject.service.user.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private final AuthenticationServiceImpl authenticationServiceImpl;
    @Autowired
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserService userService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<Map>  register(
            @RequestBody @Valid RegisterRequest request
    ) throws NullRequestException, UserExistException {
        return new ResponseEntity<>(authenticationServiceImpl.register(request), HttpStatus.OK);
    }

    @PutMapping("/verify-account")
    public ResponseEntity<Map> verifyAccount(
            @RequestParam String email,
            @RequestBody @Valid VerifyAccountRequest verifyAccountRequest
            ) throws NullRequestException, WrongOtpException, UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.verifyAccount(email, verifyAccountRequest.getOtp()), HttpStatus.OK);
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestParam String email) throws UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.regenerateOtp(email), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map> login(
            @RequestBody @Valid LoginRequest request
    ) throws NullRequestException, BadCredentials, UserNotVerifiedException, UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.login(request), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public Object refreshToken(@RequestBody @Validated RefreshTokenRequestDTO refreshToken){
        return refreshTokenServiceImpl.refreshLoginToken(refreshToken.getRefreshToken());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
    ) throws UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.forgotPassword(request), HttpStatus.OK);
    }

    @PostMapping("/forgot-password-web")
    public ResponseEntity<?> forgotPasswordWeb(
            @RequestBody @Valid ForgotPasswordRequest request
    ) throws UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.forgotPasswordWeb(request), HttpStatus.OK);
    }


    @PutMapping("/verify-account-forgot")
    public ResponseEntity<Map> verifyAccountForgot(@RequestParam String email,
                                                 @RequestBody @Valid VerifyAccountRequest verifyAccountRequest) throws NullRequestException, WrongOtpException, UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.verifyAccountPassword(email, verifyAccountRequest.getOtp()), HttpStatus.OK);
    }

    @PutMapping("/forgotpassword-web")
    public ResponseEntity<Map> forgotPasswordWeb(@RequestParam String email,
                                                 @RequestParam String token,
                                               @RequestBody @Valid ChangePasswordRequest request) throws UserNotVerifiedException, PasswordNotSameException, NullRequestException, WrongOtpException, UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.changePasswordWeb(email, token, request), HttpStatus.OK);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordAndroidRequest request,
            @RequestParam String email
    ) throws PasswordNotSameException, NullRequestException, WrongOtpException, UserNotFoundException {
        return new ResponseEntity<>(authenticationServiceImpl.changePassword(request, email), HttpStatus.OK);
    }
}
