package com.example.finalProject.repository;

import com.example.finalProject.entity.Airplane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AirplaneRepository extends JpaRepository<Airplane, UUID> {
    @Query(value = "select * from airplanes\n" +
            "where code ilike ?1 and name ilike ?2 and deleted_date is null",
            nativeQuery = true)
    public Page<Airplane> searchAll(String code, String name, Pageable pageable);

    @Query(value = "select airplanes.* from airplanes \n" +
            "join airplane_classes ac on airplanes.id = ac.airplane_id\n" +
            "join companies c on c.id = airplanes.company_id\n" +
            "where ac.airplane_class = ?1 and ac.capacity > ?2  and c.name ilike ?3",
            nativeQuery = true)
    public List<Airplane> airplaneList(String airplaneClass, int capacity, String maskapai);

    @Query(value = "select min(airplane_price + airplane_class_price + airplane_flight_time_price) as minimum_price from airplanes\n" +
            "join airplane_classes ac on airplanes.id = ac.airplane_id\n" +
            "join airplane_flight_times aft on airplanes.id = aft.airplane_id",
            nativeQuery = true)
    public int getMinimumPrice();

    @Query(value = "select \n" +
            "\tmin(airplane_price + airplane_class_price + airplane_flight_time_price) \n" +
            "\t+ coalesce((select date_price from baseprice_dates bd \n" +
            "\twhere bd.date_time = ?3),0)\n" +
            "\t+ coalesce((select airport_price from baseprice_airports ba\n" +
            "\twhere departure_code ilike ?1 and ba.arrival_code ilike ?2),0)\n" +
            "from airplanes\n" +
            "join airplane_classes ac on airplanes.id = ac.airplane_id\n" +
            "join airplane_flight_times aft on airplanes.id = aft.airplane_id",
            nativeQuery = true)
    public int getMinimumPriceThatDay(String fromAirportCode, String toAirportCode, LocalDate theDate);
}
