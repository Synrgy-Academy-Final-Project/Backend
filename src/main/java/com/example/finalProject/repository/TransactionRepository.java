package com.example.finalProject.repository;

import com.example.finalProject.dto.CheckRow;
import com.example.finalProject.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "select * from transactions\n" +
            "where code ilike ?1 and name ilike ?2 and deleted_date is null",
            nativeQuery = true)
    public Page<Transaction> searchAll(String code, String name, Pageable pageable);

    @Query(value = "select c.id, c.\"name\", c.url, a.id, a.\"name\", a.code, ac.id ,ac.airplane_class, ac.capacity, atf.id, atf.flight_time  \n" +
            "from airplanes a \n" +
            "join companies c on a.company_id = c.id\n" +
            "join airplane_classes ac on a.id = ac.airplane_id \n" +
            "join airplane_flight_times atf on a.id = atf.airplane_id \n" +
            "where \n" +
            "a.deleted_date is null \n" +
            "and c.deleted_date is null \n" +
            "and ac.deleted_date is null \n" +
            "and atf.deleted_date is null \n" +
            "and a.id = ?1\n" +
            "and ac.id = ?2\n" +
            "and atf.id = ?3", nativeQuery = true)
    List<Object[]> getAirplaneConfirmDTOById(UUID airplaneId, UUID airplaneClassId, UUID airplaneTimeFlightId);

    @Query(value = "select sum(t.total_seat), t.departure_date, t.departure_time, t.arrival_date, t.arrival_time, t.airplane_id ,t.airplane_class_id , t.airplane_time_flight_id \n" +
            "from transactions t \n" +
            "where t.deleted_date is null \n" +
            "and t.airplane_id  = ?1\n" +
            "and t.airplane_class_id  = ?2\n" +
            "and t.airplane_time_flight_id  = ?3\n" +
            "and t.departure_date = ?4\n" +
            "group by t.departure_date, t.departure_time, t.arrival_date, t.arrival_time, t.airplane_id ,t.airplane_class_id , t.airplane_time_flight_id", nativeQuery = true)
    List<Object[]> getTotalSeatTransactionAirplane(UUID airplaneId, UUID airplaneClassId, UUID airplaneTimeFlightId, LocalDate date);


    @Query("select new com.example.finalProject.dto.CheckRow(count (*)) from Transaction ac where ac.deletedDate is null ")
    CheckRow checkRow();

    @Query(value = "select t.company_name as \"companyName\", t.url as \"companyUrl\", t.airplane_class as \"airplaneClass\", concat('ID - ',t.airplane_code)  as \"airplaneCode\", \n" +
            "t.order_code as \"orderCode\", t.departure_time as \"departureTime\", t.departure_date as \"departureDate\", t.arrival_time as \"arrivalTime\", t.arrival_date as \"arrivalDate\", \n" +
            "concat(a.city,' (',t.departure_code,')') as \"departureCityCode\", a.\"name\" as \"departureAirportName\", a.country as \"departureCountry\", \n" +
            "concat(a2.city, ' (', t.arrival_code,')') as \"arrivalCityCode\", a2.\"name\" as \"arrivalAirportName\", a2.country as \"arrivalCountry\",\n" +
            "concat(ud.first_name ,' ',ud.last_name) as \"passengerName\" , \n" +
            "t.airplane_class as \"airplaneClass\", 'asdaasd' as \"ticketNumber\", '10' as \"gate\", 'A1' as \"seat\"\n" +
            "from transactions t join passengers p on p.transaction_id = t.id and to_char(t.created_date, 'YYYY-MM-DD HH24:MI') = to_char(p.created_date, 'YYYY-MM-DD HH24:MI') \n" +
            "join users_details ud on p.user_details_id = ud.id\n" +
            "join airports a on t.departure_code = a.code\n" +
            "join airports a2 on t.departure_code = a2.code \n" +
            "join payments p2 on t.id = p2.transaction_id\n" +
            "where lower(p2.transaction_status) = lower('settlement') \n" +
            "and p2.transaction_id = ?1\n" +
            "and t.user_id = ?2 " +
            "and t.deleted_date is null \n" +
            "and ud.deleted_date is null\n" +
            "and a.deleted_date is null\n" +
            "and p2.deleted_date is null", nativeQuery = true)
    List<Object[]> getDataTransactionUser(UUID transactionId, UUID userId);
}
