package com.example.finalProject.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull
    private String fullName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String role;
}
