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

    public ResponseDTO dataNotFound(String object){
        String msg = "relevant "+object+" data couldn't be found";
        return new ResponseDTO(404, msg);
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

    public Map fail(Object obj) {
        Map map = new HashMap();
        map.put("data", obj);
        map.put("status", 200);// jadiin patokan succrss
        map.put("message", "fail");
        return map;
    }

}
