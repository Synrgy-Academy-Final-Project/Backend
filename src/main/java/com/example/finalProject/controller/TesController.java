package com.example.finalProject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tes")
public class TesController {
    @GetMapping
    public String tes(){
        return "Tes Running";
    }
}
