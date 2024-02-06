package com.example.finalProject.model.user;

import com.example.finalProject.entity.SavedPassenger;
import com.example.finalProject.entity.Transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
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
    @JsonIgnore
    private UUID id;

    private String email;
    @JsonIgnore
    private String password;

    @JsonIgnore
    private Timestamp createdDate;

    @JsonIgnore
    private Timestamp updatedDate;

    @JsonIgnore
    private Timestamp deletedDate;

    @JsonIgnore
    private String otp;

    @JsonIgnore
    private Timestamp otpGeneratedTime;

    @JsonIgnore
    private boolean userActive;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRole", referencedColumnName = "id")
    private Role role;
//    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userDetailId", referencedColumnName = "id")
    private UserDetails usersDetails;
  
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    List<Transaction> transaction;

    @OneToMany(mappedBy = "user")
    List<SavedPassenger> savedPassenger;

}
