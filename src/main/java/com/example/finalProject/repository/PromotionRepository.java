package com.example.finalProject.repository;

import com.example.finalProject.entity.Promotion;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {
    @Query(value = "select * from promotions " +
            "where (lower(code) like lower(concat('%', ?1, '%')) or lower(title) like lower(concat('%', ?2, '%'))) " +
            "and deleted_date is null", nativeQuery = true)
    public Page<Promotion> searchAll(String code, String title, Pageable pageable);

}
