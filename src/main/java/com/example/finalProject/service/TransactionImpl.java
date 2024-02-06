package com.example.finalProject.service;

import com.example.finalProject.dto.*;
//import com.example.finalProject.entity.Flight;
import com.example.finalProject.entity.*;
import com.example.finalProject.model.user.User;
import com.example.finalProject.model.user.UserDetails;
//import com.example.finalProject.repository.FlightRepository;
import com.example.finalProject.repository.*;
import com.example.finalProject.repository.user.UserDetailsRepository;
import com.example.finalProject.repository.user.UserRepository;
import com.example.finalProject.security.service.UserDetailsImpl;
import com.example.finalProject.service.user.UsersServiceImpl;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.DataInput;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TransactionImpl {

    private final Response response;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UsersServiceImpl usersServiceImpl;
    private final PassengerRepository passengerRepository;
    private final SavedPassengerRepository savedPassengerRepository;
    private final PromotionRepository promotionRepository;
    private final AirplaneAdditionalServiceRepository airplaneAdditionalServiceRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final GeneralFunction generalFunction;
    private final TransactionAirplaneAdditionalServiceRepository transactionAirplaneAdditionalServiceRepository;

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

        Transaction savedTransaction = (Transaction) saveResponse.getData();
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

    public ResponseDTO save(TransactionEntityDTO request) throws IOException {
        try {
            List<UserDetailsRequest> userDetails = request.getUserDetails();

            System.out.println(userDetails.size());
            userDetails.stream().forEach(additionalUserDTO -> System.out.println(additionalUserDTO.getDateOfBirth()));

            List<DataMatureDTO> dataMature = new ArrayList<>();
            Integer child = 0;
            Integer mature = 0;
            Integer totalAdditionalBaggage = 0;
            for (int i = 0; i<userDetails.size(); i++){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                LocalDate dateOfBirth = LocalDate.parse(dateFormat.format(userDetails.get(i).getDateOfBirth()), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                Period age = Period.between(dateOfBirth, LocalDate.now());
                if (age.getYears()>2){
                    if (userDetails.get(i).getAirplaneAdditionalId() != null){
                        Optional<AirplaneAdditionalService> check = airplaneAdditionalServiceRepository.findById(userDetails.get(i).getAirplaneAdditionalId());
                        if (check.isEmpty()){
                            return response.dataNotFound("Additional Baggage");
                        }
                        Integer quantity = check.get().getQuantity();
                        Integer price = check.get().getPrice();
                        totalAdditionalBaggage = totalAdditionalBaggage + price;
                        dataMature.add(new DataMatureDTO(userDetails.get(i).getFirstName(), userDetails.get(i).getLastName(),
                                dateOfBirth, quantity, price));
                    }
                    mature++;

                }
                else{
                    child++;
                }
            }
            System.out.println(totalAdditionalBaggage);
            System.out.println(dataMature);
            System.out.println(mature +" "+ child);
            ModelMapper modelMapper = new ModelMapper();
            Transaction transaction = modelMapper.map(request, Transaction.class);
//        Transaction transaction = new Transaction();

            if (!request.getAirplaneId().equals(null) && !request.getAirplaneClassId().equals(null) && !request.getAirplaneTimeFLightId().equals(null)) {
                System.out.println("tes");
//                Optional<Airplane> checkAirplane = airplaneRepository.findById(request.getAirplaneId());
//                Optional<AirplaneClass> checkAirplaneClass = airplaneClassRepository.findById(request.getAirplaneClassId());
//                Optional<AirplaneFlightTime> checkAirplaneFlightTime = airplaneFlightTimeRepository.findById(request.getAirplaneTimeFLightId());
//                System.out.println(checkAirplane);
//                if (checkAirplane.isEmpty() && checkAirplaneClass.isEmpty() && checkAirplaneFlightTime.isEmpty()) {
//                    return response.dataNotFound("Data Airplane");
//                }
//                Airplane airplane = checkAirplane.get();
//                System.out.println(airplane);
//                AirplaneClass airplaneClass = checkAirplaneClass.get();
//                AirplaneFlightTime airplaneFlightTime = checkAirplaneFlightTime.get();

                List<Object[]> checkAirplaneConfirmDTO = transactionRepository.getAirplaneConfirmDTOById(request.getAirplaneId(), request.getAirplaneClassId(), request.getAirplaneTimeFLightId());
                CheckRow checkRows = transactionRepository.checkRow();
                System.out.println(checkAirplaneConfirmDTO);
                List<Object[]> checkTotalSeatTransactionAirplane = transactionRepository.getTotalSeatTransactionAirplane(request.getAirplaneId(), request.getAirplaneClassId(), request.getAirplaneTimeFLightId(), request.getDepartureDate());
                Optional<User> checkUserData = userRepository.findById(request.getUserId());
                if (checkUserData.isEmpty()) {
                    return response.dataNotFound("User");
                }
//            if (checkAirplaneConfirmDTO.isEmpty()) {
//                return response.dataNotFound("Data Airplane");
//            }
//            if (checkTotalSeatTransactionAirplane.isEmpty()){
//                return response.dataNotFound("Transaction");
//            }

                List<AirplaneConfirmDTO> airplaneData = checkAirplaneConfirmDTO.stream().map(array -> new AirplaneConfirmDTO(
                        (UUID) array[0],
                        (String) array[1],
                        (String) array[2],
                        (UUID) array[3],
                        (String) array[4],
                        (String) array[5],
                        (UUID) array[6],
                        (String) array[7],
                        (Integer) array[8],
                        (UUID) array[9],
                        (Time) array[10]
                )).toList();

                if (airplaneData.isEmpty()){
                    return response.dataNotFound("Data Airplane (Airplane ID, Airplane Class ID, Airplane Time Flight ID)");
                }

                List<TotalSeatDTO> totalSeatData = checkTotalSeatTransactionAirplane.stream().map(array -> new TotalSeatDTO(
                        (Long) array[0],
                        (Date) array[1],
                        (Time) array[2],
                        (Date) array[3],
                        (Time) array[4],
                        (UUID) array[5],
                        (UUID) array[6],
                        (UUID) array[7]
                )).toList();
                Long totalSeat = 0L;
                if (!totalSeatData.isEmpty()){
                    totalSeat = totalSeatData.get(0).getTotalSeatTransaction();
                }
                if (checkRows.getRow() == 0 || totalSeat <= airplaneData.get(0).getCapacity() &&
                        (mature + totalSeat) <= airplaneData.get(0).getCapacity()) {
                    System.out.println("masuk");
                    if (checkUserData.get().getUsersDetails().equals(null)){
                        response.errorDTO(500, "Update your profile");
                    }
                    transaction.setUser(checkUserData.get());
                    if (!request.getCompanyName().isEmpty() && !request.getUrl().isEmpty() &&
                            !request.getAirplaneName().isEmpty() && !request.getAirplaneCode().isEmpty() &&
                            !request.getAirplaneClassId().equals(null) && !request.getAirplaneClass().isEmpty()) {
                        System.out.println("masuk2");
                        if (request.getAirplaneId().equals(airplaneData.get(0).getAirplaneId())) {
//                        Optional<Airplane> byId = airplaneRepository.findById(airplaneData.get(0).getAirplaneId());
                            transaction.setAirplaneId(airplaneData.get(0).getAirplaneId());
                        }
                        System.out.println(transaction);
                        if (request.getAirplaneName().equals(airplaneData.get(0).getAirplaneName())) {
                            transaction.setAirplaneName(airplaneData.get(0).getAirplaneName());
                        }
                        if (request.getAirplaneCode().equals(airplaneData.get(0).getAirplaneCode())) {
                            transaction.setAirplaneCode(airplaneData.get(0).getAirplaneCode());
                        }
                        if (request.getAirplaneClassId().equals(airplaneData.get(0).getAirplaneClassId())) {
//                        Optional<AirplaneClass> byId = airplaneClassRepository.findById(airplaneData.get(0).getAirplaneClassId());
                            transaction.setAirplaneClassId(airplaneData.get(0).getAirplaneClassId());
                        }
                        if (request.getAirplaneClass().equals(airplaneData.get(0).getAirplaneClass())) {
                            transaction.setAirplaneClass(airplaneData.get(0).getAirplaneClass());
                        }
                        if (request.getAirplaneTimeFLightId().equals(airplaneData.get(0).getAirplaneFlightTimeId())) {
//                            Optional<AirplaneFlightTime> byId = airplaneFlightTimeRepository.findById(airplaneData.get(0).getAirplaneFlightTimeId());
                            transaction.setAirplaneTimeFlightId(airplaneData.get(0).getAirplaneFlightTimeId());
                        }
                    }
                    if (!request.getDepartureCode().isEmpty() && !request.getDepartureDate().equals(null)
                            && !request.getDepartureTime().equals(null) && request.getArrivalCode().isEmpty() &&
                            !request.getArrivalDate().equals(null) && !request.getArrivalTime().equals(null)) {
                        transaction.setDepartureCode(request.getDepartureCode());
                        transaction.setDepartureDate(request.getDepartureDate());
                        transaction.setDepartureTime(request.getDepartureTime());
                        transaction.setArrivalCode(request.getArrivalCode());
                        transaction.setArrivalDate(request.getArrivalDate());
                        transaction.setArrivalTime(request.getArrivalTime());
                    }
                    if (!request.getUserDetails().isEmpty()) {
                        transaction.setTotalSeat(mature);
                    }
                    Integer disc = 0;
                    if (!request.getCodePromo().isEmpty()){
                        Optional<Promotion> promotionByCode = promotionRepository.findPromotionByCode(request.getCodePromo());
                        if (promotionByCode.isEmpty()){
                            return response.dataNotFound("Promo");
                        }
                        transaction.setPromotion(promotionByCode.get());
                        disc = promotionByCode.get().getDiscount();
                    }
                    System.out.println("disc " + disc);
                    Integer totalTicket = (mature * request.getPriceFlight()) + (child * (request.getPriceFlight()-(request.getPriceFlight() * 20/100))) + totalAdditionalBaggage;
                    System.out.println("total tiket " + totalTicket);
                    Integer total = totalTicket - (totalTicket * disc/100);
                    System.out.println("total "  + total);
                    transaction.setTotalPrice(total);

                    Transaction result = transactionRepository.save(transaction);


                    if (userDetails.isEmpty()) {
                        return response.dataNotFound("Passenger");
                    }
                    for (UserDetailsRequest u : userDetails) {
                        ResponseDTO result1 = usersServiceImpl.createUserDetail(u);

                        if (result1.getStatus() == 200) {
                            passengerRepository.save(new Passenger(transactionRepository.findById(result.getId()).get(), (UserDetails) result1.getData()));

                            SavedPassenger savedPassengerData = savedPassengerRepository.findByUserAndUserDetail(request.getUserId(), ((UserDetails) result1.getData()).getId());
                            if (savedPassengerData == null){
                                savedPassengerData = new SavedPassenger();
                            }
                            savedPassengerData.setUser(result.getUser());
                            savedPassengerData.setUserDetails((UserDetails) result1.getData());
                            savedPassengerRepository.save(savedPassengerData);

                        } else {
                            throw new IOException(result1.getMessage());
                        }
                    }
                    System.out.println("Hitung data mature with additional baggage");
                    if (!dataMature.isEmpty()){
                        for (int i = 0; i<dataMature.size();i++){
                            UserDetails getUserDetails = userDetailsRepository.findByFirstNameAndLastNameAndDoB(generalFunction.createLikeQuery(dataMature.get(i).getFirstName()),
                                    generalFunction.createLikeQuery(dataMature.get(i).getLastName()),
                                    Date.from(dataMature.get(i).getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            System.out.println(getUserDetails);
                            Optional<Passenger> check = passengerRepository.findPassengerByUserDetailsAndTransaction(getUserDetails, result);
                            if (check.isEmpty()){
                                return response.dataNotFound("Passenger");
                            }
                            System.out.println(check.get());
                            transactionAirplaneAdditionalServiceRepository.save(new TransactionAirplaneAdditionalService(result, check.get(), dataMature.get(i).getQty(), dataMature.get(i).getPrice()));

                        }
                    }

                    return response.suksesDTO(result);
                } else {
                    return response.errorDTO(503,"Capacity from this airplane has been full");
                }
            }
        }catch (IOException e){
            return response.errorDTO(400, e.getMessage());
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
        return null;
    }

//    @Transactional
//    public ResponseDTO save(TransactionEntityDTO transaction) {
//        int totalPrice = 0;
//        int capacity;
//        int totalSeat;
//        Flight flight1Data = null;
//        Flight flight2Data = null;
//        List<UserDetails> userDetails = transaction.getUserDetails();
//
//        try {
//            ModelMapper modelMapper = new ModelMapper();
//            Transaction convertTotransaction = modelMapper.map(transaction, Transaction.class);
//
//            Optional<User> checkUserData = userRepository.findById(transaction.getUserId());
//            if (checkUserData.isEmpty()) {
//                return response.dataNotFound("User");
//            }
//            convertTotransaction.setUser(checkUserData.get());
//
//            Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
//            if (checkFlight1Data.isEmpty()) {
//                return response.dataNotFound("Flight1");
//            }
//            flight1Data = checkFlight1Data.get();
//            convertTotransaction.setFlight1(flight1Data);
//            capacity = flight1Data.getCapacity();
//            totalSeat = transaction.getTotalSeat();
//            if (capacity < totalSeat) {
//                return response.errorDTO(422, "Not Enough Seat");
//            }
//            flight1Data.setCapacity(capacity - totalSeat);
//            totalPrice += flight1Data.getPrice() * totalSeat;
//
//            if (transaction.getFlight2Id() != null) {
//                Optional<Flight> checkFlight2Data = flightRepository.findById(transaction.getFlight2Id());
//                if (checkFlight2Data.isEmpty()) {
//                    return response.dataNotFound("Flight2");
//                }
//                flight2Data = checkFlight2Data.get();
//                convertTotransaction.setFlight2(flight2Data);
//                capacity = flight2Data.getCapacity();
//                totalSeat = transaction.getTotalSeat();
//                if (capacity < totalSeat) {
//                    return response.errorDTO(422, "Not Enough Seat");
//                }
//                flight2Data.setCapacity(capacity - totalSeat);
//                totalPrice += flight2Data.getPrice() * totalSeat;
//            }
//            convertTotransaction.setTotalPrice(totalPrice);
//
////            updating data
//            flightRepository.save(flight1Data);
//
//            if(flight2Data != null){
//                flightRepository.save(flight2Data);
//            }
//
//            Transaction result = transactionRepository.save(convertTotransaction);
//
//            if (userDetails.isEmpty()) {
//                return response.dataNotFound("Passenger");
//            }
//            for (UserDetails u : userDetails) {
//                ResponseDTO result1 = usersServiceImpl.createUserDetail(u);
//                if (result1.getStatus() == 200) {
//                    passengerRepository.save(new Passenger(transactionRepository.findById(result.getId()).get(), (UserDetails) result1.getData()));
//                } else {
//                    throw new IOException(result1.getMessage());
//                }
//            }
//
//            return response.suksesDTO(result);
//        }catch (IOException e){
//            return response.errorDTO(400, e.getMessage());
//        }catch (Exception e){
//            return response.errorDTO(500, e.getMessage());
//        }
//    }
//
//    public ResponseDTO findById(UUID id) {
//        Optional<Transaction> checkData= transactionRepository.findById(id);
//        if (checkData.isEmpty()){
//            return response.dataNotFound("Transaction");
//        }else{
//            return response.suksesDTO(checkData.get());
//        }
//    }
//    @Transactional
//    public ResponseDTO update(UUID id, TransactionEntityDTO transaction) {
//        int capacity;
//        int totalPrice = 0;
//        int totalSeat;
//        Flight formerFlight1 = null;
//        Flight formerFlight2 = null;
//        Flight flight1Data = null;
//        Flight flight2Data = null;
//        try {
//            Optional<Transaction> checkData = transactionRepository.findById(id);
//            if (checkData.isEmpty()) {
//                return response.dataNotFound("Transaction");
//            }
//
//            Transaction updatedTransaction = checkData.get();
//
//            if (transaction.getTotalSeat() != null) {
//                totalSeat = transaction.getTotalSeat();
//            } else {
//                totalSeat = updatedTransaction.getTotalSeat();
//            }
//
//            if (transaction.getUserId() != null) {
//                Optional<User> checkUserData = userRepository.findById(transaction.getUserId());
//                if (checkUserData.isEmpty()) {
//                    return response.dataNotFound("User");
//                }
//                updatedTransaction.setUser(checkUserData.get());
//            }
//            if (transaction.getPaymentId() != null) {
//                Optional<Payment> checkPaymentData = paymentRepository.findById(transaction.getPaymentId());
//                if (checkPaymentData.isEmpty()) {
//                    return response.dataNotFound("Payment");
//                }
//                updatedTransaction.setPayment(checkPaymentData.get());
//            }
//
//            if (transaction.getFlight1Id() != null) {
//                Optional<Flight> checkFlight1Data = flightRepository.findById(transaction.getFlight1Id());
//                if (checkFlight1Data.isEmpty()) {
//                    return response.dataNotFound("Flight1");
//                }
//                flight1Data = checkFlight1Data.get();
//                capacity = flight1Data.getCapacity();
//                if (capacity < totalSeat) {
//                    return response.errorDTO(422, "Not Enough Seat");
//                }
//                flight1Data.setCapacity(capacity - totalSeat);
//                totalPrice += flight1Data.getPrice() * totalSeat;
//
//                flightRepository.save(flight1Data);
////                reversing
//                formerFlight1 = updatedTransaction.getFlight1();
//                formerFlight1.setCapacity(formerFlight1.getCapacity() + updatedTransaction.getTotalSeat());
//
//                updatedTransaction.setFlight1(flight1Data);
//            }
//
//            if (transaction.getFlight2Id() != null) {
//                Optional<Flight> checkFlight2Data = flightRepository.findById(transaction.getFlight2Id());
//                if (checkFlight2Data.isEmpty()) {
//                    return response.dataNotFound("Flight2");
//                }
//                flight2Data = checkFlight2Data.get();
//                capacity = flight2Data.getCapacity();
//                if (capacity < totalSeat) {
//                    return response.errorDTO(422, "Not Enough Seat");
//                }
//                flight2Data.setCapacity(capacity - totalSeat);
//                totalPrice += flight2Data.getPrice() * totalSeat;
//
//                flightRepository.save(flight2Data);
//
////                reversing
//                if (updatedTransaction.getFlight2() != null) {
//                    formerFlight2 = updatedTransaction.getFlight2();
//                    formerFlight2.setCapacity(formerFlight2.getCapacity() + updatedTransaction.getTotalSeat());
//                }
//
//                updatedTransaction.setFlight2(flight2Data);
//            }
//
//            updatedTransaction.setTotalSeat(totalSeat);
//            updatedTransaction.setTotalPrice(totalPrice);
//
//            if (formerFlight1 != null){
//                flightRepository.save(formerFlight1);
//            }
//
//            if (formerFlight2 != null){
//                flightRepository.save(formerFlight2);
//            }
//
//            return response.suksesDTO(transactionRepository.save(updatedTransaction));
//        }catch (Exception e){
//            return response.errorDTO(500, e.getMessage());
//        }
//    }
//
//    public ResponseDTO delete(UUID id) {
//        Flight flight1Data = null;
//        Flight flight2Data = null;
//
//        try{
//            Optional<Transaction> checkData = transactionRepository.findById(id);
//            if(checkData.isEmpty()){
//                return response.dataNotFound("Transaction");
//            }
//
//            Transaction deletedTransaction = checkData.get();
//            deletedTransaction.setDeletedDate(new Date());
//
//            flight1Data = deletedTransaction.getFlight1();
//            flight1Data.setCapacity(flight1Data.getCapacity() + deletedTransaction.getTotalSeat());
//            flightRepository.save(flight1Data);
//
//            if(deletedTransaction.getFlight2() != null){
//                flight2Data = deletedTransaction.getFlight2();
//                flight2Data.setCapacity(flight2Data.getCapacity() + deletedTransaction.getTotalSeat());
//                flightRepository.save(flight2Data);
//            }
//
//            return response.suksesDTO(transactionRepository.save(deletedTransaction));
//        }catch (Exception e){
//            return response.errorDTO(500, e.getMessage());
//        }
//    }
}
