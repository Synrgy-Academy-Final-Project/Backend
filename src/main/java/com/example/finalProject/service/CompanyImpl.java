package com.example.finalProject.service;

import com.example.finalProject.dto.CompanyEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Company;
import com.example.finalProject.repository.CompanyRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyImpl {
    @Autowired
    Response response;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    GeneralFunction generalFunction;

    public ResponseDTO searchAll(String query, Pageable pageable) {
        String updatedQuery = generalFunction.createLikeQuery(query);
        return response.suksesDTO(companyRepository.searchAll(updatedQuery, pageable));
    }

    public ResponseDTO save(CompanyEntityDTO company) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            Company convertToCompany = modelMapper.map(company, Company.class);
            Company result = companyRepository.save(convertToCompany);
            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Company> checkData= companyRepository.findById(id);
        if (checkData.isEmpty()){
            return response.errorDTO(404, Config.DATA_NOT_FOUND);
        }else{
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, CompanyEntityDTO company) {
        try{
            Optional<Company> checkData = companyRepository.findById(id);
            if(checkData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Company updatedCompany = checkData.get();
            updatedCompany.setName(company.getName());
            return response.suksesDTO(companyRepository.save(updatedCompany));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try{
            Optional<Company> checkData = companyRepository.findById(id);
            if(checkData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Company deletedCompany = checkData.get();
            deletedCompany.setDeletedDate(new Date());
            return response.suksesDTO(companyRepository.save(deletedCompany));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }
}
