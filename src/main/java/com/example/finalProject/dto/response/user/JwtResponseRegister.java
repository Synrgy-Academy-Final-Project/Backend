package com.example.finalProject.dto.response.user;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class JwtResponseRegister {
    private String message;
    private String type;
    private String fullName;
    private String email;
    private List<String> roles;
//    private LocalDateTime otpGeneratedTime;

    public JwtResponseRegister(String token, String fullName,String email, List<String> roles) {
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
    }
}
