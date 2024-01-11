package com.example.finalProject.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyAccountRequest {
    @NotNull(message = "otp shouldn't be null")
    private  String otp;
}
