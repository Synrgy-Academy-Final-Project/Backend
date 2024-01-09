package com.example.finalProject.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "usersDetails",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phoneNumber"),
                @UniqueConstraint(columnNames = "visa"),
                @UniqueConstraint(columnNames = "passport"),
                @UniqueConstraint(columnNames = "residentPermit"),
                @UniqueConstraint(columnNames = "NIK")
        })
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String address;
    private String gender;
    private String phoneNumber;
    private String visa;
    private String passport;
    private String residentPermit;
    private String nik;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private Timestamp deletedDate;

    @JsonIgnore
    @OneToOne(mappedBy = "usersDetails")
    private User user;
}
