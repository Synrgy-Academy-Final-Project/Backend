package com.example.finalProject.repository;

import com.example.finalProject.entity.Airport;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirportRepository extends JpaRepository<Airport, UUID> {
    @Query(value = "select * from airports " +
            "where (lower(code) like lower(concat('%', ?1, '%')) or lower(name) like lower(concat('%', ?2, '%'))) " +
            "and deleted_date is null", nativeQuery = true)
    public Page<Airport> searchAll(String code, String name, Pageable pageable);

}
