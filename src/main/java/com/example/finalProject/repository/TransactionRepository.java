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

    @Query(value = "select sum(t.seat_mature), t.departure_date, t.departure_time, t.arrival_date, t.arrival_time, t.airplane_id ,t.airplane_class_id , t.airplane_time_flight_id \n" +
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


    @Query(value = "select t.order_code as \"orderCode\", t.total_price as \"totalPrice\", \n" +
            "t.departure_code as \"departureCode\", concat(a.city, ' (',a.code,')') as \"departureCityCode\",\n" +
            "t.arrival_code as \"arrivalCode\", concat(a2.city, ' (',a2.code,')') as \"arrivalCityCode\",\n" +
            "case \n" +
            "\twhen p.transaction_status = 'settlement'  then 'Pembayaran Berhasil'\n" +
            "\twhen p.transaction_status = 'pending' then 'Menunggu Pembayaran'\n" +
            "\twhen p.transaction_status = 'failure' then 'Pembayaran Tidak Berhasil'\n" +
            "\twhen p.transaction_status = 'refund' then 'Pengembalian Pembayaran'\n" +
            "end as \"transactionStatus\"\n" +
            "from payments p \n" +
            "join transactions t on p.transaction_id = t.id \n" +
            "join airports a on a.code = t.departure_code \n" +
            "join airports a2 on a2.code = t.arrival_code \n" +
            "where t.user_id = ?1\n" +
            "and p.deleted_date is null\n" +
            "and t.deleted_date is null \n" +
            "and a.deleted_date is null\n" +
            "and a2.deleted_date is null  ",
            countQuery = "select count(*) from transaction t where t.user_id = ?1",
            nativeQuery = true)
    Page<Object[]> getDataHistoryTransaction(UUID userId, Pageable pageable);

    @Query(value = "select t.company_name as \"companyName\", t.url as \"companyUrl\", t.airplane_class as \"airplaneClass\", concat('ID - ',t.airplane_code)  as \"airplaneCode\", \n" +
            "t.order_code as \"orderCode\", t.departure_time as \"departureTime\", t.departure_date as \"departureDate\", t.arrival_time as \"arrivalTime\", t.arrival_date as \"arrivalDate\", \n" +
            "concat(a.city,' (',t.departure_code,')') as \"departureCityCode\", a.\"name\" as \"departureAirportName\", a.country as \"departureCountry\", \n" +
            "concat(a2.city, ' (', t.arrival_code,')') as \"arrivalCityCode\", a2.\"name\" as \"arrivalAirportName\", a2.country as \"arrivalCountry\",\n" +
            "concat(ud.first_name ,' ',ud.last_name) as \"passengerName\" , as2.baggage as \"baggage\", coalesce (taas.quantity, 0) as \"additionalBaggage\",\n" +
            "concat(t.company_name, ' (', t.airplane_code, ') ', t.departure_date) as \"airplaneNameCodeDate\",\n" +
            "t.price_flight as \"priceFlight\", t.seat_baby as \"seatBaby\", t.total_baby_transaction as \"totalBabyTransaction\", t.seat_mature as \"seatMature\", t.total_mature_transaction as \"totalMatureTransaction\", \n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 5 then coalesce(tot.totalItem, 0)\n" +
            "end, 0) as \"qtyItem5\",\n" +
            "coalesce (case \n" +
            "\twhen taas.price = 100000 then coalesce(taas.price, 0)\n" +
            "end, 0) as \"priceItem5\",\n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 5 and taas.price = 100000 then coalesce(tot.totalItem * taas.price, 0)\n" +
            "end, 0) as \"subTotpriceItem5\",\n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 10 then coalesce(tot.totalItem, 0)\n" +
            "end, 0) as \"qtyItem10\",\n" +
            "coalesce (case \n" +
            "\twhen taas.price = 150000 then coalesce(taas.price, 0)\n" +
            "end, 0) as \"priceItem10\",\n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 10 and taas.price = 150000 then coalesce(tot.totalItem * taas.price, 0)\n" +
            "end, 0) as \"subTotpriceItem10\",\n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 15 then coalesce(tot.totalItem, 0)\n" +
            "end, 0) as \"qtyItem15\",\n" +
            "coalesce (case \n" +
            "\twhen taas.price = 200000 then coalesce(taas.price, 0)\n" +
            "end, 0) as \"priceItem15\",\n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 15 and taas.price = 200000 then coalesce(tot.totalItem * taas.price, 0)\n" +
            "end, 0) as \"subTotpriceItem15\",\n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 20 then coalesce(tot.totalItem, 0)\n" +
            "end, 0) as \"qtyItem20\",\n" +
            "coalesce (case \n" +
            "\twhen taas.price = 250000 then coalesce(taas.price, 0)\n" +
            "end, 0) as \"priceItem20\",\n" +
            "coalesce (case \n" +
            "\twhen taas.quantity = 20 and taas.price = 250000 then coalesce(tot.totalItem * taas.price, 0)\n" +
            "end, 0) as \"subTotpriceItem20\",\n" +
            "p3.code as \"codePromo\", t.total_discount as \"totalDiscount\", concat(CAST(t.discount  AS VARCHAR), ' %') as \"discount\", t.total_price  as \"total\"\n" +
            "from transactions t \n" +
            "join passengers p on p.transaction_id = t.id and to_char(t.created_date, 'YYYY-MM-DD HH24:MI') = to_char(p.created_date, 'YYYY-MM-DD HH24:MI') \n" +
            "join users_details ud on p.user_details_id = ud.id\n" +
            "join airports a on t.departure_code = a.code\n" +
            "join airports a2 on t.arrival_code  = a2.code \n" +
            "join payments p2 on t.id = p2.transaction_id\n" +
            "join airplane_service as2 on t.airplane_class_id = as2.airplane_class_id\n" +
            "left join transaction_airplane_additional_service taas ON t.id = taas.transaction_id and p.id = taas.passenger_id \n" +
            "left join promotions p3 ON t.promotion_id = p3.id\n" +
            "left join (select count(*) as totalItem, taas2.quantity as \"qty\" from transaction_airplane_additional_service taas2 where taas2.transaction_id = ?1 group by taas2.quantity ) tot on taas.quantity = tot.qty\n" +
            "where lower(p2.transaction_status) = lower('settlement') \n" +
            "and p2.transaction_id = ?1\n" +
            "and t.user_id = ?2\n" +
            "and t.deleted_date is null \n" +
            "and p.deleted_date is null\n" +
            "and ud.deleted_date is null\n" +
            "and a.deleted_date is null\n" +
            "and p2.deleted_date is null\n" +
            "and as2.deleted_date is null\n" +
            "and taas.deleted_date is null\n" +
            "and p3.deleted_date is null", nativeQuery = true)
    List<Object[]> getDataTransactionUser(UUID transactionId, UUID userId);
}
