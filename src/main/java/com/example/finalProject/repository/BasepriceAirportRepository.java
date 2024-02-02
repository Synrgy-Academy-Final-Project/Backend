package com.example.finalProject.repository;

import com.example.finalProject.entity.BasePriceAirport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface BasepriceAirportRepository extends JpaRepository<BasePriceAirport, UUID> {
//    @Query(value = "select * from baseprice_airplanes where (lower(airplane_class) like lower(concat('%', ?1, '%'))) or (lower(airplane_price) like lower(concat('%', ?2, '%'))) and deleted_date is null", nativeQuery = true)
//    public Page<BasePriceAirplane> searchAll(String airplaneClass, String airplanePrice, Pageable pageable);

//    @Query(value = "SELECT * FROM baseprice_airplanes WHERE (LOWER(airplane_class) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(airplane_price) LIKE LOWER(CONCAT('%', ?2, '%'))) AND deleted_date IS NULL", nativeQuery = true)
//    Page<BasePriceAirplane> searchAll(String airplaneClass, String airplanePrice, Pageable pageable);

//    @Query(value = "SELECT * FROM baseprice_dates WHERE (LOWER(CAST(date_from AS TEXT)) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(CAST(date_to AS TEXT)) LIKE LOWER(CONCAT('%', ?2, '%'))) or LOWER(CAST(type AS TEXT)) LIKE LOWER(CONCAT('%', ?3, '%'))) or LOWER(CAST(date_price AS TEXT)) LIKE LOWER(CONCAT('%', ?4, '%')))AND deleted_date IS NULL", nativeQuery = true)
//    Page<BasePriceDate> searchAll(String dateFrom, String dateTo, String type, String priceDate, Pageable pageable);

    @Query(value = "select * from baseprice_airports\n" +
            "where departure_code = ?1 and arrival_code  = ?2",
            nativeQuery = true)
    public BasePriceAirport getAirportPrice(String fromAirport, String toAirport);

}
