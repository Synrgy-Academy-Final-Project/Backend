package com.example.finalProject.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String password;
    private String fullName;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private Timestamp deletedDate;
    private String otp;
    private Timestamp otpGeneratedTime;
    private boolean userActive;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role roles;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userDetailId", referencedColumnName = "id")
    private UserDetails usersDetails;
}
