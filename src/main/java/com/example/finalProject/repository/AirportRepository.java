package com.example.finalProject.repository;

import com.example.finalProject.dto.AirportSearchDTO;
import com.example.finalProject.entity.Airport;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AirportRepository extends JpaRepository<Airport, UUID> {
//    @Query(value = "select * from airports " +
//            "where (lower(code) like lower(concat('%', ?1, '%')) or lower(name) like lower(concat('%', ?2, '%'))) " +
//            "and deleted_date is null", nativeQuery = true)
//    public Page<Airport> searchAll(String code, Pageable pageable);

        @Query("select new com.example.finalProject.dto.AirportSearchDTO(" +
                "concat(a.city, ' ', '(',upper(a.code),')'), " +
                "concat(initcap(a.city),', ',initcap(a.country)), " +
                "concat(upper(a.code),' - ',initcap(a.name)), " +
                "a.code) " +
                "from Airport a " +
                "where concat(a.city, ' ', '(',upper(a.code),')') ilike concat('%',:query,'%')" +
                "or concat(initcap(a.city),', ',initcap(a.country)) ilike concat('%',:query,'%')" +
                "or concat(initcap(a.code),' - ',initcap(a.name)) ilike concat('%',:query,'%')" +
                "and a.deletedDate is null ")
    Page<AirportSearchDTO> searchAll(@Param("query")String query, Pageable pageable);
}
