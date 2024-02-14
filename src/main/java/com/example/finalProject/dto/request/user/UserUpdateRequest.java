package com.example.finalProject.dto.request.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class UserUpdateRequest {
    private UUID id;

    private String firstName;

    private String lastName;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateOfBirth;

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

    private String nationality;
}
