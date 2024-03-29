package com.example.finalProject.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull(message = "name shouldn't be null")
    private String fullName;
    @NotNull(message = "email shouldn't be null")
    @Email(message = "invalid email address")
    private String email;
    @NotNull(message = "password shouldn't be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password should be at least 8 characters long, " +
            "containing at least one uppercase, one lowercase, no whitespace characters" +
            "one digit, and one special character from the allowed set: (?=.*[@#$%^&+=!])")
    private String password;
    @NotNull(message = "role shouldn't be null")
    private String role;
}
