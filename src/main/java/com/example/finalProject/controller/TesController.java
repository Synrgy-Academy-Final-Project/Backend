package com.example.finalProject.controller;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.model.user.User;
import com.example.finalProject.repository.AirplaneRepository;
import com.example.finalProject.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tes")
public class TesController {
    @GetMapping
    public String tes(){
        return "Tes Running";
    }

    @Value("${midtrans.server.key}")
    private String midtransServerKey;

//    @GetMapping("/detail")
//    public Object findUser(Principal principal){
//        if (principal == null){
//            return "principal is null";
//        }else{
//            Optional<User> user = userRepository.findUserByEmail(principal.getName());
//            if (user.isEmpty()){
//                return "user not found";
//            }
//            return user.get().getUsersDetails();
//        }
//    }

//    @GetMapping("hash")
//    public String hashing() throws NoSuchAlgorithmException {
//        String order = "729b7c69-95a6-4a46-b6cd-80dc9a8ffb94";
//        String status = "202";
//        String gross = "8629521.00";
//        String server = midtransServerKey;
//        String passwordToHash = order + status + gross + server;
//        String generatedPassword = null;
//            MessageDigest md = MessageDigest.getInstance("SHA-512");
//            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i< bytes.length ;i++){
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            generatedPassword = sb.toString();
//        System.out.println("ded5cce3bff29be9d95cb07b58047fe643ae4edff24a0d7d3b10224b86f995725bc3026634698fe07659ffb4685c89958f4b4e855c57a26b2672ea80fbb15eb9");
//        System.out.println(generatedPassword);
//        return generatedPassword;
//    }

//    @GetMapping("minimum-price")
//    public Object minimumPrice(@ModelAttribute("fromAirportCode") String fromAirportCode,
//                            @ModelAttribute("toAirportCode") String toAirportCode,
//                            @ModelAttribute("departureDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date departureDate){
//        List<Map<String, Object>> sevenDays = new ArrayList<>();
//        LocalDate date = LocalDate.ofInstant(departureDate.toInstant(), ZoneId.systemDefault());
//        for (int i = 0; i < 7; i++){
//            Map<String, Object> data = new HashMap<>();
//            LocalDate theDate = date.plusDays(i);
//            data.put("date", theDate);
//            data.put("price", airplaneRepository.getMinimumPriceThatDay(fromAirportCode, toAirportCode, theDate));
//            sevenDays.add(data);
//        }
//        return sevenDays;
//    }

}
