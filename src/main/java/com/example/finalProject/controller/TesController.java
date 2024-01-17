package com.example.finalProject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@RestController
@CrossOrigin
@RequestMapping("/tes")
public class TesController {
    @GetMapping
    public String tes(){
        return "Tes Running";
    }

    @Value("${midtrans.server.key}")
    private String midtransServerKey;

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
}
