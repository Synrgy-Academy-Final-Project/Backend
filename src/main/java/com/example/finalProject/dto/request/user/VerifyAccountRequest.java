package com.example.finalProject.dto.request.user;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyAccountRequest {
    private  String otp;
}
