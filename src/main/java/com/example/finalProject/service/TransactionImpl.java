package com.example.finalProject.service;

import com.example.finalProject.dto.ResponseDTO;
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

    public ResponseDTO searchAll(Pageable pageable) {
        return response.suksesDTO(transactionRepository.findAll(pageable));
    }

    @Transactional
    public ResponseDTO save(TransactionEntityDTO transaction) {
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
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            convertTotransaction.setUser(checkUserData.get());

            Optional<Payment> checkPaymentData = paymentRepository.findById(transaction.getPaymentId());
            if (checkPaymentData.isEmpty()) {
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            convertTotransaction.setPayment(checkPaymentData.get());

            Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
            if (checkFlight1Data.isEmpty()) {
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            flight1Data = checkFlight1Data.get();
            convertTotransaction.setFlight1(flight1Data);
            capacity = flight1Data.getCapacity();
            totalSeat = transaction.getTotalSeat();
            if (capacity < totalSeat) {
                return response.errorDTO(404, "Not Enough Seat");
            }
            flight1Data.setCapacity(capacity - totalSeat);
            totalPrice += flight1Data.getPrice() * totalSeat;

            if (transaction.getFlight2Id() != null) {
                Optional<Flight> checkFlight2Data = flightRepository.findById(transaction.getFlight2Id());
                if (checkFlight2Data.isEmpty()) {
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                flight2Data = checkFlight2Data.get();
                convertTotransaction.setFlight2(flight2Data);
                capacity = flight2Data.getCapacity();
                totalSeat = transaction.getTotalSeat();
                if (capacity < totalSeat) {
                    return response.errorDTO(404, "Not Enough Seat");
                }
                flight2Data.setCapacity(capacity - totalSeat);
                totalPrice += flight2Data.getPrice() * totalSeat;
            }
            convertTotransaction.setTotalPrice(totalPrice);

//            updating data
            flightRepository.save(flight1Data);

            if(flight2Data != null){
                flightRepository.save(flight2Data);
            }

            Transaction result = transactionRepository.save(convertTotransaction);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Transaction> checkData= transactionRepository.findById(id);
        if (checkData.isEmpty()){
            return response.errorDTO(404, Config.DATA_NOT_FOUND);
        }else{
            return response.suksesDTO(checkData.get());
        }
    }
    @Transactional
    public ResponseDTO update(UUID id, TransactionEntityDTO transaction) {
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
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
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
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                updatedTransaction.setUser(checkUserData.get());
            }
            if (transaction.getPaymentId() != null) {
                Optional<Payment> checkPaymentData = paymentRepository.findById(transaction.getPaymentId());
                if (checkPaymentData.isEmpty()) {
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                updatedTransaction.setPayment(checkPaymentData.get());
            }

            if (transaction.getFlight1Id() != null) {
                Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
                if (checkFlight1Data.isEmpty()) {
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                flight1Data = checkFlight1Data.get();
                capacity = flight1Data.getCapacity();
                if (capacity < totalSeat) {
                    return response.errorDTO(404, "Not Enough Seat");
                }
                flight1Data.setCapacity(capacity - totalSeat);
                totalPrice += flight1Data.getPrice() * totalSeat;

                flightRepository.save(flight1Data);
//                reversing
                formerFlight1 = updatedTransaction.getFlight1();
                formerFlight1.setCapacity(formerFlight1.getCapacity() + updatedTransaction.getTotalSeat());

                updatedTransaction.setFlight1(flight1Data);
            }

            if (transaction.getFlight2Id() != null) {
                Optional<Flight> checkFlight2Data = flightRepository.findById(transaction.getFlight2Id());
                if (checkFlight2Data.isEmpty()) {
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                flight2Data = checkFlight2Data.get();
                capacity = flight2Data.getCapacity();
                if (capacity < totalSeat) {
                    return response.errorDTO(404, "Not Enough Seat");
                }
                flight2Data.setCapacity(capacity - totalSeat);
                totalPrice += flight2Data.getPrice() * totalSeat;

                flightRepository.save(flight2Data);

//                reversing
                if (updatedTransaction.getFlight2() != null) {
                    formerFlight2 = updatedTransaction.getFlight2();
                    formerFlight2.setCapacity(formerFlight2.getCapacity() + updatedTransaction.getTotalSeat());
                }

                updatedTransaction.setFlight2(flight2Data);
            }

            updatedTransaction.setTotalSeat(totalSeat);
            updatedTransaction.setTotalPrice(totalPrice);

            if (formerFlight1 != null){
                flightRepository.save(formerFlight1);
            }

            if (formerFlight2 != null){
                flightRepository.save(formerFlight2);
            }

            return response.suksesDTO(transactionRepository.save(updatedTransaction));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        Flight flight1Data = null;
        Flight flight2Data = null;

        try{
            Optional<Transaction> checkData = transactionRepository.findById(id);
            if(checkData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
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

            return response.suksesDTO(transactionRepository.save(deletedTransaction));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }
}
