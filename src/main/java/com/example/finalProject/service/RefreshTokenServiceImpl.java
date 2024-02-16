package com.example.finalProject.service;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.response.user.JwtResponseLogin;
import com.example.finalProject.entity.RefreshToken;
import com.example.finalProject.repository.RefreshTokenRepository;
import com.example.finalProject.repository.user.UserRepository;
import com.example.finalProject.security.service.JwtService;
import com.example.finalProject.security.service.UserService;
import com.example.finalProject.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl {
    @Autowired
    Response response;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String email){
        Optional<RefreshToken> optionalData = refreshTokenRepository.searchByEmail(email);
        if(optionalData.isEmpty()){
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(userRepository.findUserByEmail(email).get())
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(60*60*24*7)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file
                    .build();
            return refreshTokenRepository.save(refreshToken);
        }else{
            RefreshToken data = optionalData.get();
            data.setToken(UUID.randomUUID().toString());
            data.setExpiryDate(Instant.now().plusSeconds(60*60*24*7));
            return refreshTokenRepository.save(data);
        }

    }

    public ResponseDTO refreshLoginToken(String token){
        System.out.println(token);
        Optional<RefreshToken> optionalData = findByToken(token);
        if (optionalData.isEmpty()){
            return response.dataNotFound("RefreshToken");
        }

        RefreshToken data = optionalData.get();
        try{
            verifyExpiration(data);
        }catch(RuntimeException e){
            return response.errorDTO(401, e.getMessage());
        }



        UserDetails user = userService.loadUserByUsername(data.getUser().getEmail());

        JwtResponseLogin jwtResponseLogin = new JwtResponseLogin();
        jwtResponseLogin.setToken(jwtService.generateToken(user));
        jwtResponseLogin.setType("Bearer");
        jwtResponseLogin.setEmail(data.getUser().getEmail());
        jwtResponseLogin.setRoles(user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        jwtResponseLogin.setRefreshToken(token);

        return response.suksesDTO(jwtResponseLogin);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}
