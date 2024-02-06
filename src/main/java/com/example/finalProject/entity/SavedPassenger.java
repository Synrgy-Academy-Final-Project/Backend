package com.example.finalProject.entity;

import com.example.finalProject.model.user.User;
import com.example.finalProject.model.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Where(clause = "deleted_date is null")
public class SavedPassenger extends AbstractDate {

    public SavedPassenger(User user, UserDetails userDetails){
        this.user = user;
        this.userDetails = userDetails;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    @ToString.Exclude
    User user;

    @NotNull
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    @ToString.Exclude
    UserDetails userDetails;
}
