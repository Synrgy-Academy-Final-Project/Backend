package com.example.finalProject.dto.request.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    @NotNull(message = "password shouldn't be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password should be at least 8 characters long, " +
            "containing at least one uppercase, one lowercase, no whitespace characters" +
            "one digit, and one special character from the allowed set: (?=.*[@#$%^&+=!])")
    private String newPassword;
    @NotNull(message = "password shouldn't be null")
    private String confirmationPassword;
}
