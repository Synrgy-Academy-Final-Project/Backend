package com.example.finalProject.repository;

import com.example.finalProject.entity.Payment;
import com.example.finalProject.entity.Promotion;
import com.example.finalProject.entity.RefreshToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    @Query(value = "select refresh_token.* from refresh_token \n" +
            "join users on users.id = refresh_token.user_id\n" +
            "where users.email  = ?1",
            nativeQuery = true)
    Optional<RefreshToken> searchByEmail(String email);
}
