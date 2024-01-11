package com.example.finalProject.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordRequest {
    @NotNull(message = "email shouldn't be null")
    @Email(message = "invalid email address")
    private String email;
}
