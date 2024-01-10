package com.example.finalProject.utils;

import com.example.finalProject.dto.ResponseDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class Response {

    public Map sukses(Object obj){
        Map map = new HashMap();
        map.put("data", obj);
        map.put("status", 200);// jadiin patokan succrss
        map.put("message", "Success");
        return map;
    }

    public ResponseDTO suksesDTO(Object obj){
        return new ResponseDTO(200, "success", obj);
    }

    public Map error(Object obj, Object code){
        Map map = new HashMap();
        map.put("status", code);
        map.put("message", obj);
        return map;
    }

    public ResponseDTO errorDTO(int status, String message){
        return new ResponseDTO(status, message);
    }

    public Map Error(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", 404);
        return map;
    }

    public Map templateSukses(Object objek){
        Map map = new HashMap();
        map.put("data", objek);
        map.put("message", "sukses");
        map.put("status", 200);
        return map;
    }

    public Map templateEror(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", 404);
        return map;
    }
    public Map notFound(Object objek){
        Map map = new HashMap();
        map.put("message", objek);
        map.put("status", 404);
        return map;
    }
    public boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        String passRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*()-_+=!]).{8,}$";

        return password.matches(passRegex);
    }

    public Map fail(String message) {
        Map map = new HashMap();
        map.put("message", message);
        map.put("status", 400);
        return map;
    }
}
