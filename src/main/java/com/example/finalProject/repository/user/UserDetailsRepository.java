package com.example.finalProject.repository.user;

import com.example.finalProject.model.user.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
//    @Query(value = "select * from users_details\n" +
//            "where name ilike ?1 and deleted_date is null",
//            nativeQuery = true)
//    public Page<UserDetails> searchAll(String query, Pageable pageable);

    @Query(value = "select * from users_details\n" +
            "where deleted_date is null",
            nativeQuery = true)
    public Page<UserDetails> searchAll(String query, Pageable pageable);

    @Query(value = "select * from users_details ud \n" +
            "where first_name ilike ?1 \n" +
            "\tand (last_name ilike ?2 or last_name is null)\n" +
            "\tand date(date_of_birth) = ?3",
            nativeQuery = true)
    public UserDetails findByFirstNameAndLastNameAndDoB(String firstName, String lastName, Date DoB);
}
