package com.example.finalProject.service;

import com.example.finalProject.dto.MidtransRequestDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.TransactionEntityDTO;
import com.example.finalProject.entity.Flight;
import com.example.finalProject.entity.Passenger;
import com.example.finalProject.entity.Payment;
import com.example.finalProject.entity.Transaction;
import com.example.finalProject.model.user.User;
import com.example.finalProject.model.user.UserDetails;
import com.example.finalProject.repository.FlightRepository;
import com.example.finalProject.repository.PassengerRepository;
import com.example.finalProject.repository.PaymentRepository;
import com.example.finalProject.repository.TransactionRepository;
import com.example.finalProject.repository.user.UserRepository;
import com.example.finalProject.security.service.UserDetailsImpl;
import com.example.finalProject.service.user.UsersServiceImpl;
import com.example.finalProject.utils.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class TransactionImpl {
    @Autowired
    Response response;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    UsersServiceImpl usersServiceImpl;
    @Autowired
    PassengerRepository passengerRepository;

    @Value("${midtrans.server.key}")
    private String midtransServerKey;

    public ResponseDTO searchAll(Pageable pageable) {
        return response.suksesDTO(transactionRepository.findAll(pageable));
    }

    public ResponseDTO createMidtransRequest(TransactionEntityDTO transaction) throws IOException, InterruptedException {
        ResponseDTO saveResponse = save(transaction);
        if (saveResponse.getStatus() >= 400){
            return saveResponse;
        }

        Transaction savedTransaction = (Transaction) save(transaction).getData();

        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", savedTransaction.getId());
        transactionDetails.put("gross_amount", savedTransaction.getTotalPrice());

        User customer = savedTransaction.getUser();
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name", customer.getUsersDetails().getFirstName());
        customerDetails.put("last_name", customer.getUsersDetails().getLastName());
        customerDetails.put("email", customer.getEmail());
        customerDetails.put("phone", customer.getUsersDetails().getPhoneNumber());

        MidtransRequestDTO midtransRequest = new MidtransRequestDTO();
        midtransRequest.setTransaction_details(transactionDetails);
        midtransRequest.setCustomer_details(customerDetails);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(midtransRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://app.sandbox.midtrans.com/snap/v1/transactions"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " +
                        Base64.getEncoder().encodeToString((midtransServerKey).getBytes()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        String midtransRedirectUrl = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        ObjectMapper mapper = new ObjectMapper();
        Map result = mapper.readValue(midtransRedirectUrl, Map.class);

        return response.suksesDTO(result);
    }

    @Transactional
    public ResponseDTO save(TransactionEntityDTO transaction) {
        int totalPrice = 0;
        int capacity;
        int totalSeat;
        Flight flight1Data = null;
        Flight flight2Data = null;
        List<UserDetails> userDetails = transaction.getUserDetails();

        try {
            ModelMapper modelMapper = new ModelMapper();
            Transaction convertTotransaction = modelMapper.map(transaction, Transaction.class);

            Optional<User> checkUserData = userRepository.findById(transaction.getUserId());
            if (checkUserData.isEmpty()) {
                return response.dataNotFound("User");
            }
            convertTotransaction.setUser(checkUserData.get());

            Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
            if (checkFlight1Data.isEmpty()) {
                return response.dataNotFound("Flight1");
            }
            flight1Data = checkFlight1Data.get();
            convertTotransaction.setFlight1(flight1Data);
            capacity = flight1Data.getCapacity();
            totalSeat = transaction.getTotalSeat();
            if (capacity < totalSeat) {
                return response.errorDTO(422, "Not Enough Seat");
            }
            flight1Data.setCapacity(capacity - totalSeat);
            totalPrice += flight1Data.getPrice() * totalSeat;

            if (transaction.getFlight2Id() != null) {
                Optional<Flight> checkFlight2Data = flightRepository.findById(transaction.getFlight2Id());
                if (checkFlight2Data.isEmpty()) {
                    return response.dataNotFound("Flight2");
                }
                flight2Data = checkFlight2Data.get();
                convertTotransaction.setFlight2(flight2Data);
                capacity = flight2Data.getCapacity();
                totalSeat = transaction.getTotalSeat();
                if (capacity < totalSeat) {
                    return response.errorDTO(422, "Not Enough Seat");
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

            if (userDetails.isEmpty()) {
                return response.dataNotFound("Passenger");
            }
            for (UserDetails u : userDetails) {
                ResponseDTO result1 = usersServiceImpl.createUserDetail(u);
                if (result1.getStatus() == 200) {
                    passengerRepository.save(new Passenger(transactionRepository.findById(result.getId()).get(), (UserDetails) result1.getData()));
                } else {
                    throw new IOException(result1.getMessage());
                }
            }

            return response.suksesDTO(result);
        }catch (IOException e){
            return response.errorDTO(400, e.getMessage());
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Transaction> checkData= transactionRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("Transaction");
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
                return response.dataNotFound("Transaction");
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
                    return response.dataNotFound("User");
                }
                updatedTransaction.setUser(checkUserData.get());
            }
            if (transaction.getPaymentId() != null) {
                Optional<Payment> checkPaymentData = paymentRepository.findById(transaction.getPaymentId());
                if (checkPaymentData.isEmpty()) {
                    return response.dataNotFound("Payment");
                }
                updatedTransaction.setPayment(checkPaymentData.get());
            }

            if (transaction.getFlight1Id() != null) {
                Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
                if (checkFlight1Data.isEmpty()) {
                    return response.dataNotFound("Flight1");
                }
                flight1Data = checkFlight1Data.get();
                capacity = flight1Data.getCapacity();
                if (capacity < totalSeat) {
                    return response.errorDTO(422, "Not Enough Seat");
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
                    return response.dataNotFound("Flight2");
                }
                flight2Data = checkFlight2Data.get();
                capacity = flight2Data.getCapacity();
                if (capacity < totalSeat) {
                    return response.errorDTO(422, "Not Enough Seat");
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
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        Flight flight1Data = null;
        Flight flight2Data = null;

        try{
            Optional<Transaction> checkData = transactionRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("Transaction");
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
            return response.errorDTO(500, e.getMessage());
        }
    }
}
