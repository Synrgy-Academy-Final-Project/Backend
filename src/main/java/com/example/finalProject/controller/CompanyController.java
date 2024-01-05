package com.example.finalProject.controller;

import com.example.finalProject.DTO.CompanyEntityDTO;
import com.example.finalProject.service.CompanyImpl;
import com.example.finalProject.utils.GeneralFunction;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {
    @Autowired
    CompanyImpl companyImpl;

    @GetMapping({"", "/"})
    public ResponseEntity<Object> searchCompany(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "100") int pageSize,
                                                 @RequestParam(defaultValue = "") String sortBy,
                                                 @ModelAttribute("name") String name){
        Pageable pageable;
        if (sortBy.isEmpty()){
            System.out.println("true");
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            System.out.println("false");
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        return new ResponseEntity<>(companyImpl.searchAll(name, pageable), HttpStatus.OK);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Map> addCompany(@RequestBody @Validated CompanyEntityDTO company){
        return new ResponseEntity<>(companyImpl.save(company), HttpStatus.OK);
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<Map> findCompany(@PathVariable UUID id){
        return new ResponseEntity<>(companyImpl.findById(id), HttpStatus.OK);
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<Map> updateCompany(@PathVariable UUID id, @RequestBody  @Validated CompanyEntityDTO company){
        return new ResponseEntity<>(companyImpl.update(id, company), HttpStatus.OK);
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<Map> deleteCompany(@PathVariable UUID id){
        return new ResponseEntity<>(companyImpl.delete(id), HttpStatus.OK);
    }
}
