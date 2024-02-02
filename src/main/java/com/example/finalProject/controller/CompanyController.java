package com.example.finalProject.controller;

import com.example.finalProject.dto.CompanyEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.service.CompanyImpl;
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
@CrossOrigin(origins = "*")
@RequestMapping("/company")
@Slf4j
public class CompanyController {
    @Autowired
    CompanyImpl companyImpl;

    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> searchCompany(@RequestParam(defaultValue = "0") int pageNumber,
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
        ResponseDTO result = companyImpl.searchAll(name, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ResponseDTO> addCompany(@RequestBody @Validated CompanyEntityDTO company){
        ResponseDTO result = companyImpl.save(company);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> findCompany(@PathVariable UUID id){
        ResponseDTO result = companyImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> updateCompany(@PathVariable UUID id, @RequestBody  @Validated CompanyEntityDTO company){
        ResponseDTO result = companyImpl.update(id, company);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({"{id}", "{id}/"})
    public ResponseEntity<ResponseDTO> deleteCompany(@PathVariable UUID id){
        ResponseDTO result = companyImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
