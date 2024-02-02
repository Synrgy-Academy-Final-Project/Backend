package com.example.finalProject.repository;

import com.example.finalProject.dto.CheckRow;
import com.example.finalProject.dto.ETicketDTO;
import com.example.finalProject.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "select * from transactions\n" +
            "where code ilike ?1 and name ilike ?2 and deleted_date is null",
            nativeQuery = true)
    public Page<Transaction> searchAll(String code, String name, Pageable pageable);

//    @Query("select new com.example.finalProject.dto.ETicketDTO('fullName', a3.name, a3.code, " +
//            "f1.airplaneClass, f1.departureDate," +
//            "a1.name, a1.code, a1.city, a1.country, " +
//            "f1.arrivalDate, a2.name, a2.code, a2.city, a2.country," +
//            "t2.id, t2.gate, t2.seat) from User u " +
//            "JOIN u.transaction t JOIN t.flight1 f1 JOIN f1.fromAirport a1 JOIN f1.toAirport a2 join f1.airplane a3 " +
//            "join a3.company c join t.ticket t2 where u.id=:userId")
//    List<ETicketDTO> getAllUserTransaction(@Param("userId") UUID userId);

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

//    @Query(value = "select sum(t.total_seat), t.airplane_id ,t.airplane_class_id , t.airplane_time_flight_id \n" +
//            "from transactions t \n" +
//            "where t.deleted_date is null \n" +
//            "and t.airplane_id = ?1\n" +
//            "and t.airplane_class_id  = ?2\n" +
//            "and t.airplane_time_flight_id  = ?3\n" +
//            "group by t.airplane_id, t.airplane_class_id , t.airplane_time_flight_id ", nativeQuery = true)
//    List<Object[]> getTotalSeatTransactionAirplane(UUID airplaneId, UUID airplaneClassId, UUID airplaneTimeFlightId);

    @Query(value = "select sum(t.total_seat), t.departure_date, t.departure_time, t.arrival_date, t.arrival_time, t.airplane_id ,t.airplane_class_id , t.airplane_time_flight_id \n" +
            "from transactions t \n" +
            "where t.deleted_date is null \n" +
            "and t.airplane_id  = ?1\n" +
            "and t.airplane_class_id  = ?2\n" +
            "and t.airplane_time_flight_id  = ?3\n" +
            "and t.departure_date = ?4\n" +
            "group by t.departure_date, t.departure_time, t.arrival_date, t.arrival_time, t.airplane_id ,t.airplane_class_id , t.airplane_time_flight_id", nativeQuery = true)
    List<Object[]> getTotalSeatTransactionAirplane(UUID airplaneId, UUID airplaneClassId, UUID airplaneTimeFlightId, LocalDate date);


    @Query("select new com.example.finalProject.dto.CheckRow(count (*)) from Transaction ac ")
    CheckRow checkRow();
}
