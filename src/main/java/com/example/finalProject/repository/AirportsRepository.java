package com.example.finalProject.repository;

import com.example.finalProject.entity.Airports;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirportsRepository extends JpaRepository<Airports, UUID> {
    @Query(value = "select * from airports\n" +
            "where code ilike ?1 and name ilike ?2 and deleted_date is null", nativeQuery = true)
    public Page<Airports> searchAll(String code, String name, Pageable pageable);
}
