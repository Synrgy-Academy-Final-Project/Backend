package com.example.finalProject.service.user;

import com.example.finalProject.dto.request.user.*;
import com.example.finalProject.dto.response.user.*;
import com.example.finalProject.exception.*;
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
import java.util.Optional;

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
    public Map register(RegisterRequest request) throws NullRequestException, UserExistException {
        Map map = new HashMap<>();
        if (!request.getFullName().isEmpty() && !request.getEmail().isEmpty() && !request.getPassword().isEmpty() && !request.getRole().isEmpty()){
            Optional<User> check = userRepository.findUserByEmail(request.getEmail());
            if (check.isPresent()){
                throw new UserExistException();
            }
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
                    .role(roles)
                    .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                    .updatedDate(Timestamp.valueOf(LocalDateTime.now()))
                    .otp(passwordEncoder.encode(otp))
                    .otpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()))
                    .userActive(false)
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
        }else {
            throw new NullRequestException();
        }
        return map;
    }

    @Override
    public Map verifyAccount(String email, String otp) throws NullRequestException, WrongOtpException, UserNotFoundException {
        Map map;
        User user = getIdUser(email);
        UserDetails userDetails = userService.loadUserByUsername(email);
        List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if (otp.isEmpty()){
            throw new NullRequestException();
        }
        JwtResponseRegister jwtResponseRegister = new JwtResponseRegister();
        if (passwordEncoder.matches(otp, user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            user.setUserActive(true);
            userRepository.save(user);

            jwtResponseRegister.setMessage("Account has been verified");
            jwtResponseRegister.setType("Bearer");
            jwtResponseRegister.setFullName(user.getFullName());
            jwtResponseRegister.setEmail(user.getEmail());
            jwtResponseRegister.setRoles(rolesList);
            return response.sukses(jwtResponseRegister);
        }
        throw new WrongOtpException();
    }

    @Override
    public Map regenerateOtp(String email) throws UserNotFoundException {
        User user = getIdUser(email);
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(passwordEncoder.encode(otp));
        user.setOtpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Email sent... please verify account within 1 minute");
        return response.sukses(messageResponse);
    }

    @Override
    public Map login(LoginRequest request) throws NullRequestException, BadCredentials, UserNotVerifiedException, UserNotFoundException {
        Map map = new HashMap<>();
        if (!request.getEmail().isEmpty() && !request.getPassword().isEmpty()){
            User userId = getIdUser(request.getEmail());
            if (request.getEmail().equals(userId.getEmail()) && passwordEncoder.matches(request.getPassword(), userId.getPassword())){
                if (userId.isUserActive()) {
                    Authentication authenticate = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword()
                            )
                    );

                    UserDetails user = userService.loadUserByUsername(request.getEmail());

                    UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();
                    JwtResponseLogin jwtResponseLogin = new JwtResponseLogin();

                    var jwtToken = jwtService.generateToken(user);
                    jwtResponseLogin.setToken(jwtToken);
                    jwtResponseLogin.setType("Bearer");
                    jwtResponseLogin.setFullName(userId.getFullName());
                    jwtResponseLogin.setEmail(userId.getEmail());
                    jwtResponseLogin.setRoles(roles);
                    map = response.sukses(jwtResponseLogin);
                }else{
                    throw new UserNotVerifiedException();
                }
            }else{
                throw new BadCredentials();
            }
        }else{
            throw new NullRequestException();
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
    public User getIdUser(String name) throws UserNotFoundException {
//        Map map;
//        Optional<User> userByEmail = userRepository.findUserByEmail(name);
//        if (userByEmail.isEmpty()){
//            return response.error("User not found with this email: " + name, HttpStatus.valueOf(String.valueOf(HttpStatus.NOT_FOUND)));
//        }
//        return response.sukses(userByEmail);
        return userRepository.findUserByEmail(name).orElseThrow(() ->
                new UserNotFoundException("User not found with this email: " + name));
    }


    @Override
    public Map changePassword(ChangePasswordAndroidRequest request, String email) throws NullRequestException, WrongOtpException, PasswordNotSameException, UserNotFoundException {

        User user = getIdUser(email);
        MessageResponse messageResponse = new MessageResponse();
        if (request.getToken().isEmpty()){
            throw new NullRequestException();
        }
        if (passwordEncoder.matches(request.getToken(), user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            if (user.isUserActive()){
                // check if the two new passwords are the same
                if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                    throw new PasswordNotSameException();
                }

                // update the password
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));

                // save the new password
                userRepository.save(user);
                messageResponse.setMessage("Your password has been change");
                return response.sukses(messageResponse);
            }
        }
        throw new WrongOtpException();
    }

    @Override
    public Map forgotPassword(ForgotPasswordRequest request) throws UserNotFoundException {
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
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Check your email to verification using OTP");
        return response.sukses(messageResponse);
    }

    @Override
    public Map forgotPasswordWeb(ForgotPasswordRequest request) throws UserNotFoundException {
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
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Check your email to verification using OTP");
        return response.sukses(messageResponse);
    }

    @Override
    public Map verifyAccountPassword(String email, String otp) throws NullRequestException, WrongOtpException, UserNotFoundException {
        User user = getIdUser(email);
        String token = otpUtil.generateToken();
        if (otp.isEmpty()){
            throw new NullRequestException();
        }
        if (passwordEncoder.matches(otp, user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (60)) {
            user.setOtp(passwordEncoder.encode(token));
            user.setOtpGeneratedTime(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(user);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(token);
            return response.sukses(tokenResponse);
        }
        throw new WrongOtpException();
    }

    @Override
    public Map changePasswordWeb(String email, String token, ChangePasswordRequest request) throws PasswordNotSameException, UserNotVerifiedException, WrongOtpException, NullRequestException, UserNotFoundException {
        User user = getIdUser(email);
        if (request.getNewPassword().isEmpty() && request.getConfirmationPassword().isEmpty()){
            throw new NullRequestException();
        }
        if (passwordEncoder.matches(token, user.getOtp()) && Duration.between(user.getOtpGeneratedTime().toLocalDateTime(),
                LocalDateTime.now()).getSeconds() < (300)) {
            if (user.isUserActive()){
                // check if the two new passwords are the same
                if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                    throw new PasswordNotSameException();
                }

                // update the password
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));

                // save the new password
                userRepository.save(user);
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Your password has been change");
                return response.sukses(messageResponse);
            }
            throw new UserNotVerifiedException();
        }
        throw new WrongOtpException();
    }
}
