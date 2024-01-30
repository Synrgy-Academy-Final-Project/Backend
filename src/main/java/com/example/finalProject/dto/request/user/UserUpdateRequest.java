package com.example.finalProject.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class UserUpdateRequest {
    @NotNull
    private String address;
    @NotNull
    private String gender;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String visa;
    @NotNull
    private String passport;
    @NotNull
    private String residentPermit;
    @NotNull
    private String nik;
}
