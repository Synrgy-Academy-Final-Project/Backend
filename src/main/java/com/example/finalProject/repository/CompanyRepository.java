package com.example.finalProject.repository;

import com.example.finalProject.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query(value = "select * from companies\n" +
            "where name ilike ?1 and deleted_date is null",
            nativeQuery = true)
    public Page<Company> searchAll(String query, Pageable pageable);
}
