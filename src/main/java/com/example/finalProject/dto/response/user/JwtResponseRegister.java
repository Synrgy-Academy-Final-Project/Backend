package com.example.finalProject.dto.response.user;

import lombok.*;
import lombok.experimental.Accessors;

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
    private String email;
    private List<String> roles;
//    private LocalDateTime otpGeneratedTime;
}
