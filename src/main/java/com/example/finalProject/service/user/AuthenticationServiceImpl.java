package com.example.finalProject.service.user;

import com.example.finalProject.dto.request.user.*;
import com.example.finalProject.dto.response.user.*;
import com.example.finalProject.model.user.ERole;
import com.example.finalProject.model.user.Role;
import com.example.finalProject.model.user.User;
import com.example.finalProject.repository.user.RoleRepository;
import com.example.finalProject.repository.user.UserRepository;
import com.example.finalProject.security.service.JwtService;
import com.example.finalProject.security.service.UserDetailsImpl;
import com.example.finalProject.security.service.UserService;
import com.example.finalProject.security.util.EmailUtil;
import com.example.finalProject.security.util.OtpUtil;
import com.nimbusds.jose.crypto.PasswordBasedDecrypter;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuhenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

    @Override
    public JwtResponseRegister register(RegisterRequest request) {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(request.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        Role roles = addRole(ERole.valueOf(request.getRole()));
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .updatedDate(Timestamp.valueOf(LocalDateTime.now()))
                .otp(passwordEncoder.encode(otp))
                .otpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()))
                .userActive(false)
                .build();
        userRepository.save(user);
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
        List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
//        var jwtToken = jwtService.generateToken(userDetails);
        return JwtResponseRegister.builder()
                .message("User not verify")
                .type("Bearer")
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(rolesList)
                .build();
    }

    @Override
    public JwtResponseRegister verifyAccount(String email, String otp) {
        User user = getIdUser(email);
        UserDetails userDetails = userService.loadUserByUsername(email);
        List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (passwordEncoder.matches(otp, user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            user.setUserActive(true);
            userRepository.save(user);
            return JwtResponseRegister.builder()
                    .message("Account has been verified")
                    .type("Bearer")
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .roles(rolesList)
                    .build();
        }
        return JwtResponseRegister.builder()
                .message("Account has not been verified")
                .type("Bearer")
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(rolesList)
                .build();
    }

    @Override
    public RegenerateOtpResponse regenerateOtp(String email) {
        User user = getIdUser(email);
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            return RegenerateOtpResponse.builder()
                    .message("Unable to send otp please try again")
                    .build();
        }
        user.setOtp(passwordEncoder.encode(otp));
        user.setOtpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        return RegenerateOtpResponse.builder()
                .message("Email sent... please verify account within 1 minute")
                .build();
    }

    @Override
    public JwtResponseLogin login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserDetails user = userService.loadUserByUsername(request.getEmail());
        User userId = getIdUser(request.getEmail());
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (userId.isUserActive()) {
            var jwtToken = jwtService.generateToken(user);
            return JwtResponseLogin.builder()
                    .token(jwtToken)
                    .type("Bearer")
                    .fullName(userId.getFullName())
                    .email(userId.getEmail())
                    .roles(roles)
                    .build();
        }
        return JwtResponseLogin.builder()
                .token("User not verify")
                .type("Bearer")
                .fullName(userId.getFullName())
                .email(userId.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public Role addRole(ERole role) {
        Role roles= roleRepository.findRoleByName(role).get();

        return roles;
    }

    @Override
    public User getIdUser(String name) {
        return userRepository.findUserByEmail(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email: " + name));
    }

    @Override
    public JwtResponseVerifyForgot changePassword(ChangePasswordRequest request, String email) {

        User user = getIdUser(email);
        if (user.isUserActive()){
            // check if the two new passwords are the same
            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                return JwtResponseVerifyForgot.builder()
                        .message("Password are not same")
                        .build();
            }

            // update the password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));

            // save the new password
            userRepository.save(user);
            return JwtResponseVerifyForgot.builder()
                    .message("Your password has been change")
                    .build();
        }
        return JwtResponseVerifyForgot.builder()
                .message("Your account not veriffy")
                .build();
    }

    @Override
    public JwtResponseForgotPassword forgotPassword(ForgotPasswordRequest request) {
        User userId = getIdUser(request.getEmail());
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(request.getEmail(), otp);
//            userId.setUserActive(false);
            userId.setOtp(passwordEncoder.encode(otp));
            userId.setOtpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(userId);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        return JwtResponseForgotPassword.builder()
                .message("Check your email to verification using OTP")
                .build();
    }

    @Override
    public JwtResponseVerifyForgot verifyAccountPassword(String email, String otp) {
        User user = getIdUser(email);
        if (passwordEncoder.matches(otp, user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
//            user.setUserActive(true);
            userRepository.save(user);
            return JwtResponseVerifyForgot.builder()
                    .message("Enter your new password")
                    .build();
        }
        return JwtResponseVerifyForgot.builder()
                .message("The code can't be used")
                .build();
    }
}
