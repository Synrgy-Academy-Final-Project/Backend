package com.example.finalProject.service.user;

import com.example.finalProject.dto.request.user.ChangePasswordRequest;
import com.example.finalProject.dto.request.user.ForgotPasswordRequest;
import com.example.finalProject.dto.request.user.LoginRequest;
import com.example.finalProject.dto.request.user.RegisterRequest;
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
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.Response;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Response response;

    @Override
    public Map register(RegisterRequest request) {
        Map map = new HashMap<>();
        try{
            if (!request.getFullName().isEmpty() && !request.getEmail().isEmpty() && !request.getPassword().isEmpty() && !request.getRole().isEmpty()){
                String otp = otpUtil.generateOtp();
                try {
                    emailUtil.sendOtpEmail(request.getEmail(), otp);
                } catch (MessagingException e) {
                    throw new RuntimeException("Unable to send otp please try again");
                }
                if (!response.isValidPassword(request.getPassword())){
                    map = response.fail("Password should be at least 8 characters long, " +
                            "containing at least one uppercase, one lowercase," +
                            "one digit, and one special character from the allowed set: !@#$%^&*()-_+=");
                } else if (!response.isValidEmail(request.getEmail())) {
                    map = response.fail("Email not valid");
                }else {
                    Role roles = addRole(ERole.valueOf(request.getRole()));
                    var user = User.builder()
                            .fullName(request.getFullName())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(roles)
                            .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                            .updatedDate(Timestamp.valueOf(LocalDateTime.now()))
                            .otp(passwordEncoder.encode(otp))
                            .otpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()))
                            .userActive(false)
//                            .usersDetails(com.example.finalProject.model.user.UserDetails.builder().id(UUID.randomUUID()).build())
                            .build();
                    userRepository.save(user);
                    UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
                    List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                    JwtResponseRegister jwtResponseRegister = new JwtResponseRegister();
                    jwtResponseRegister.setMessage("User not verify");
                    jwtResponseRegister.setType("Bearer");
                    jwtResponseRegister.setFullName(user.getFullName());
                    jwtResponseRegister.setEmail(user.getEmail());
                    jwtResponseRegister.setRoles(rolesList);

                    map = response.sukses(jwtResponseRegister);
                }
            }else {
                map = response.fail("You must fill the field");
            }
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
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
    public Map login(LoginRequest request) {
        Map map = new HashMap<>();
        try {
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
            JwtResponseLogin jwtResponseLogin = new JwtResponseLogin();

            if (!request.getEmail().isEmpty() && !request.getPassword().isEmpty()){
                if (request.getEmail().equals(userId.getEmail())){
                    if (userId.isUserActive()) {
                        var jwtToken = jwtService.generateToken(user);
                        jwtResponseLogin.setToken(jwtToken);
                        jwtResponseLogin.setType("Bearer");
                        jwtResponseLogin.setFullName(userId.getFullName());
                        jwtResponseLogin.setEmail(userId.getEmail());
                        jwtResponseLogin.setRoles(roles);
                        map = response.sukses(jwtResponseLogin);
                    }else{
                        map = response.fail("User not verify");
                    }
                }else {
                    map = response.fail("Email or password is wrong");
                }
            }else {
                map = response.fail("You must fill the field");
            }

        }catch(Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    @Override
    public Role addRole(ERole role) {
        Role getRole = roleRepository.findRoleByName(role).get();
//        Set<Role> roleHashSet = new HashSet<>();
//        roleHashSet.add(getRole);
        return getRole;
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
    public JwtResponseForgotPassword forgotPasswordWeb(ForgotPasswordRequest request) {
        User userId = getIdUser(request.getEmail());
        String token = otpUtil.generateToken();
        try {
            emailUtil.sendOtpEmailLink(request.getEmail(), token);
            userId.setOtp(passwordEncoder.encode(token));
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
    public TokenResponse verifyAccountPassword(String email, String otp) {
        User user = getIdUser(email);
        String token = otpUtil.generateToken();
        if (passwordEncoder.matches(otp, user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            user.setOtp(token);
            user.setOtpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(token)
                    .build();
        }
        return TokenResponse.builder()
                .token("The code can't be used")
                .build();
    }

    @Override
    public JwtResponseVerifyForgot changePasswordWeb(String email, String token, ChangePasswordRequest request) {
        User user = getIdUser(email);
        if (passwordEncoder.matches(token, user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (300)) {
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
        return JwtResponseVerifyForgot.builder()
                .message("The token can't be used")
                .build();



    }
}
