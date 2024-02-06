package com.example.finalProject.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String phoneNumber;
    private String visa;
    private String passport;
    private String residentPermit;
    private String NIK;
    private Date dateOfBirth;
    private UUID airplaneAdditionalId;

}
