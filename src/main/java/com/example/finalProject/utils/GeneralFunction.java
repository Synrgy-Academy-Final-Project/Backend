package com.example.finalProject.utils;

import org.springframework.stereotype.Component;

@Component
public class GeneralFunction {
    public String createLikeQuery(String raw){
        if (raw.isEmpty()){
            return "%";
        }else{
            String result = raw.replace(' ', '%');
            result = '%'+result+'%';
            return result;
        }

    }
}
