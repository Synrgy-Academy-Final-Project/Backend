package com.example.finalProject.model.user;

import com.example.finalProject.entity.Passenger;
import com.example.finalProject.entity.SavedPassenger;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String phoneNumber;
    private String visa;
    private String passport;
    private String residentPermit;
    private String NIK;
    @Column(columnDefinition = "date")
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date dateOfBirth;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private Timestamp deletedDate;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "usersDetails")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "userDetails")
    List<Passenger> passenger;

    @JsonIgnore
    @OneToMany(mappedBy = "userDetails")
    List<SavedPassenger> savedPassenger;
}
