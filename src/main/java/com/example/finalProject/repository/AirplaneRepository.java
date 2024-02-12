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
            "\tmin(airplane_price + airplane_class_price + airplane_flight_time_price)\n" +
            "\t+ coalesce((select date_price from baseprice_dates bd \n" +
            "\twhere bd.date_time = ?3),0)\n" +
            "\t+ coalesce((select airport_price from baseprice_airports ba\n" +
            "\twhere departure_code ilike ?1 and ba.arrival_code ilike ?2 and deleted_date is null),0)\n" +
            "from airplanes a \n" +
            "join companies c on a.company_id = c.id \n" +
            "join airplane_classes ac on a.id = ac.airplane_id \n" +
            "join airplane_flight_times atf on a.id = atf.airplane_id \n" +
            "join airplane_service as2 on ac.id = as2.airplane_class_id \n" +
            "where initcap(ac.airplane_class) = initcap(?4)",
            nativeQuery = true)
    public int getMinimumPriceThatDay(String fromAirportCode, String toAirportCode, LocalDate theDate, String airplaneClass);

    @Query(value = "select aas.id as \"airplaneId\", aas.\"type\" as \"type\", aas.quantity as \"qty\", aas.price as \"price\"  \n" +
            "from airplane_additional_service aas \n" +
            "join airplanes a on aas.airplane_id = a.id \n" +
            "where a.id = ?1\n" +
            "and aas.deleted_date is null\n" +
            "and a.deleted_date is null", nativeQuery = true)
    List<Object[]> getDataAirplaneAdditionalBaggage(UUID airplaneId);
}
