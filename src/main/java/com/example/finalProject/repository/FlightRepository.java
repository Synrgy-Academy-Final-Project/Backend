package com.example.finalProject.repository;

import com.example.finalProject.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Flight, UUID> {
    @Query(value = "select flights.* from flights\n" +
            "inner join airports a on from_airport_id = a.id \n" +
            "inner join airports a2 on to_airport_id =a2.id\n" +
            "where \n" +
            "\ta.code ilike ?1 \n" +
            "\tand a2.code ilike ?2 \n" +
            "\tand DATE(departure_date) = ?3 \n" +
            "\tand DATE(arrival_date) = ?4 \n" +
            "\tand airplane_class ilike ?5 \n" +
            "\tand capacity >= ?6",
            nativeQuery = true)
    public Page<Flight> searchAll(String fromAirportCode, String toAirportCode, Date departureDate, Date arrivalDate, String airplaneClass, int capacity, Pageable pageable);

}
