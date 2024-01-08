package com.example.finalProject.dto.response.user;

import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class UserUpdateResponse {
    private String address;
    private String gender;
    private String phoneNumber;
    private String visa;
    private String passport;
    private String residentPermit;
    private String nik;
}
