package com.example.finalProject.utils;

import com.example.finalProject.dto.MidtransResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Component
public class GeneralFunction {
    @Value("${midtrans.server.key}")
    private String midtransServerKey;
    public String createLikeQuery(String raw){
        if (raw.isEmpty()){
            return "%";
        }else{
            String result = raw.replace(' ', '%');
            result = '%'+result+'%';
            return result;
        }

    }

    public boolean validateMidtransResponse(MidtransResponseDTO midtransResponse) {
        String serverKey = midtransServerKey;

        String passwordToHash = midtransResponse.getOrder_id().toString() + midtransResponse.getStatus_code() + midtransResponse.getGross_amount() + serverKey;
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        System.out.println(generatedPassword);
//        System.out.println(midtransResponse.getSignature_key());
        return Objects.equals(generatedPassword, midtransResponse.getSignature_key());
    }
}
