package com.example.finalProject.service;

import com.example.finalProject.DTO.CompanyEntityDTO;
import com.example.finalProject.entity.Company;
import com.example.finalProject.repository.CompanyRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ObjectInputFilter;
import java.util.*;

@Service
public class CompanyImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    GeneralFunction generalFunction;

    public Page<Company> searchAll(String query, Pageable pageable) {
        String updatedQuery = generalFunction.createLikeQuery(query);
        return companyRepository.searchAll(updatedQuery, pageable);
    }

    public Map save(CompanyEntityDTO company) {
        Map map = new HashMap<>();

        try{
            ModelMapper modelMapper = new ModelMapper();
            Company convertToCompany = modelMapper.map(company, Company.class);
            Company result = companyRepository.save(convertToCompany);
            map = response.sukses(result);
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map findById(UUID id) {
        Map map;

        Optional<Company> checkData= companyRepository.findById(id);
        if (checkData.isEmpty()){
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        }else{
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map update(UUID id, CompanyEntityDTO company) {
        Map map;
        try{
            Optional<Company> checkData = companyRepository.findById(id);
            if(checkData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Company updatedCompany = checkData.get();
            updatedCompany.setName(company.getName());
            map = response.sukses(companyRepository.save(updatedCompany));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map delete(UUID id) {
        Map map;
        try{
            Optional<Company> checkData = companyRepository.findById(id);
            if(checkData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Company deletedCompany = checkData.get();
            deletedCompany.setDeletedDate(new Date());
            map = response.sukses(companyRepository.save(deletedCompany));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
