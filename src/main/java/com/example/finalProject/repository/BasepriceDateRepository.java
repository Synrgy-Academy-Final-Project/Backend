package com.example.finalProject.repository;

import com.example.finalProject.entity.BasePriceDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.UUID;

public interface BasepriceDateRepository extends JpaRepository<BasePriceDate, UUID> {
    @Query(value = "select * from baseprice_dates bd where date_from between ?1::timestamp and ?2::timestamp \n" +
            "or date_to  between ?3::timestamp and ?4::timestamp \n" +
            "or date_price::integer between ?5::integer and ?6::integer or lower(\"type\") = ?7 and deleted_date is null"
            , nativeQuery = true)
    Page<BasePriceDate> searchAll(String dateFrom1, String dateFrom2, String dateTo1, String dateTo2, String priceDown, String priceUp, String type, Pageable pageable);

//    @Query(value = "select * from baseprice_dates bd where date_from between ?1::timestamp and ?2::timestamp " +
//            "or date_to between ?3::timestamp and ?4::timestamp " +
//            "or date_price::integer between ?5::integer and ?6::integer or lower(\"type\") = ?7 and deleted_date is null",
//            nativeQuery = true)
//    Page<BasePriceDate> searchAll(String dateFrom1, String dateFrom2, String dateTo1, String dateTo2, String priceDown, String priceUp, String type, Pageable pageable);

    @Query(value = "select * from baseprice_dates bd \n" +
            "\twhere bd.date_from <= ?1 and bd.date_to >= ?1"
            , nativeQuery = true)
    BasePriceDate getDatePrice(Date date);


}
