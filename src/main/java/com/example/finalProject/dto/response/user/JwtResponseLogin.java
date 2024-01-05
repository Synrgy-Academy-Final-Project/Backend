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
public class JwtResponseLogin {
    private String token;
    private String type;
    private String fullName;
    private String email;
    private List<String> roles;
}
