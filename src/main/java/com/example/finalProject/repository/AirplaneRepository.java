package com.example.finalProject.repository;

import com.example.finalProject.entity.Airplane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AirplaneRepository extends JpaRepository<Airplane, UUID> {
    @Query(value = "select * from airplanes\n" +
            "where code ilike ?1 and name ilike ?2 and deleted_date is null",
            nativeQuery = true)
    public Page<Airplane> searchAll(String code, String name, Pageable pageable);
}
