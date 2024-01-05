package com.example.finalProject.dto.response.user;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class JwtResponseForgotPassword {
    private String message;

}
