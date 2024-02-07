package com.example.finalProject.security.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class OtpUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String CHARACTERS_ORDERCODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);

        while (output.length() < 6) {
            output = "0" + output;
        }
        return output;
    }

    public String generateToken(){
        int length = 16;
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            System.out.println(randomIndex);
            token.append(CHARACTERS.charAt(randomIndex));
        }

        return token.toString();
    }

    public String generatorderCode(){
        int length = 8;
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS_ORDERCODE.length());
            System.out.println(randomIndex);
            token.append(CHARACTERS_ORDERCODE.charAt(randomIndex));
        }

        return token.toString();
    }
}