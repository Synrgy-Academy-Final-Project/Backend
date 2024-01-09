package com.example.finalProject.service;

import com.example.finalProject.dto.TransactionEntityDTO;
import com.example.finalProject.entity.Flight;
import com.example.finalProject.entity.Payment;
import com.example.finalProject.entity.Transaction;
import com.example.finalProject.model.user.User;
import com.example.finalProject.repository.*;
import com.example.finalProject.repository.user.UserRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    GeneralFunction generalFunction;

    public Page<Transaction> searchAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Transactional
    public Map save(TransactionEntityDTO transaction) {
        Map map = new HashMap<>();
        int totalPrice = 0;
        int capacity;
        int totalSeat;
        Flight flight1Data = null;
        Flight flight2Data = null;

        try {
            ModelMapper modelMapper = new ModelMapper();
            Transaction convertTotransaction = modelMapper.map(transaction, Transaction.class);

            Optional<User> checkUserData = userRepository.findById(transaction.getUserId());
            if (checkUserData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            convertTotransaction.setUser(checkUserData.get());

            Optional<Payment> checkPaymentData = paymentRepository.findById(transaction.getPaymentId());
            if (checkPaymentData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            convertTotransaction.setPayment(checkPaymentData.get());

            Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
            if (checkFlight1Data.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            flight1Data = checkFlight1Data.get();
            convertTotransaction.setFlight1(flight1Data);
            capacity = flight1Data.getCapacity();
            totalSeat = transaction.getTotalSeat();
            if (capacity < totalSeat) {
                return response.error("Not Enough Seat", Config.EROR_CODE_404);
            }
            flight1Data.setCapacity(capacity - totalSeat);
            totalPrice += capacity * totalSeat;

            if (transaction.getFlight2Id() != null) {
                Optional<Flight> checkFlight2Data = flightRepository.findById(transaction.getFlight2Id());
                if (checkFlight2Data.isEmpty()) {
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                flight2Data = checkFlight2Data.get();
                convertTotransaction.setFlight2(flight2Data);
                capacity = flight2Data.getCapacity();
                totalSeat = transaction.getTotalSeat();
                if (capacity < totalSeat) {
                    return response.error("Not Enough Seat", Config.EROR_CODE_404);
                }
                flight2Data.setCapacity(capacity - totalSeat);
                totalPrice += capacity * totalSeat;
            }
            convertTotransaction.setTotalPrice(totalPrice);

//            updating data
            flightRepository.save(flight1Data);

            if(flight2Data != null){
                flightRepository.save(flight2Data);
            }

            Transaction result = transactionRepository.save(convertTotransaction);

            map = response.sukses(result);
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map findById(UUID id) {
        Map map;

        Optional<Transaction> checkData= transactionRepository.findById(id);
        if (checkData.isEmpty()){
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        }else{
            map = response.sukses(checkData.get());
        }
        return map;
    }
    @Transactional
    public Map update(UUID id, TransactionEntityDTO transaction) {
        Map map;
        int capacity;
        int totalPrice = 0;
        int totalSeat;
        Flight formerFlight1 = null;
        Flight formerFlight2 = null;
        Flight flight1Data = null;
        Flight flight2Data = null;
        try {
            Optional<Transaction> checkData = transactionRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Transaction updatedTransaction = checkData.get();

            if (transaction.getTotalSeat() != null) {
                totalSeat = transaction.getTotalSeat();
            } else {
                totalSeat = updatedTransaction.getTotalSeat();
            }

            if (transaction.getUserId() != null) {
                Optional<User> checkUserData = userRepository.findById(transaction.getUserId());
                if (checkUserData.isEmpty()) {
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                updatedTransaction.setUser(checkUserData.get());
            }
            if (transaction.getPaymentId() != null) {
                Optional<Payment> checkPaymentData = paymentRepository.findById(transaction.getPaymentId());
                if (checkPaymentData.isEmpty()) {
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                updatedTransaction.setPayment(checkPaymentData.get());
            }

            if (transaction.getFlight1Id() != null) {
                Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
                if (checkFlight1Data.isEmpty()) {
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                flight1Data = checkFlight1Data.get();
                capacity = flight1Data.getCapacity();
                if (capacity < totalSeat) {
                    return response.error("Not Enough Seat", Config.EROR_CODE_404);
                }
                flight1Data.setCapacity(capacity - totalSeat);
                totalPrice += capacity * totalSeat;

//                reversing
                formerFlight1 = updatedTransaction.getFlight1();
                formerFlight1.setCapacity(formerFlight1.getCapacity() + updatedTransaction.getTotalSeat());

                updatedTransaction.setFlight1(flight1Data);
            }

            if (transaction.getFlight2Id() != null) {
                Optional<Flight> checkFlight2Data = flightRepository.findById(transaction.getFlight2Id());
                if (checkFlight2Data.isEmpty()) {
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                flight2Data = checkFlight2Data.get();
                capacity = flight2Data.getCapacity();
                if (capacity < totalSeat) {
                    return response.error("Not Enough Seat", Config.EROR_CODE_404);
                }
                flight2Data.setCapacity(capacity - totalSeat);
                totalPrice += capacity * totalSeat;

//                reversing
                if (updatedTransaction.getFlight2() != null) {
                    formerFlight2 = updatedTransaction.getFlight2();
                    formerFlight2.setCapacity(formerFlight2.getCapacity() + updatedTransaction.getTotalSeat());
                }

                updatedTransaction.setFlight2(flight2Data);
            }

//            updating data
            if (flight1Data != null){
                flightRepository.save(flight1Data);
            }

            if (flight2Data != null){
                flightRepository.save(flight2Data);
            }

            if (formerFlight1 != null){
                flightRepository.save(formerFlight1);
            }

            if (formerFlight2 != null){
                flightRepository.save(formerFlight2);
            }

            map = response.sukses(transactionRepository.save(updatedTransaction));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map delete(UUID id) {
        Map map;
        Flight flight1Data = null;
        Flight flight2Data = null;

        try{
            Optional<Transaction> checkData = transactionRepository.findById(id);
            if(checkData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Transaction deletedTransaction = checkData.get();
            deletedTransaction.setDeletedDate(new Date());

            flight1Data = deletedTransaction.getFlight1();
            flight1Data.setCapacity(flight1Data.getCapacity() + deletedTransaction.getTotalSeat());
            flightRepository.save(flight1Data);

            if(deletedTransaction.getFlight2() != null){
                flight2Data = deletedTransaction.getFlight2();
                flight2Data.setCapacity(flight2Data.getCapacity() + deletedTransaction.getTotalSeat());
                flightRepository.save(flight2Data);
            }

            map = response.sukses(transactionRepository.save(deletedTransaction));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
