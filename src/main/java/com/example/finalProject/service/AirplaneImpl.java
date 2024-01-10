package com.example.finalProject.service;

import com.example.finalProject.dto.AirplaneEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.Company;
import com.example.finalProject.repository.AirplaneRepository;
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
public class AirplaneImpl {
    @Autowired
    Response response;
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    GeneralFunction generalFunction;

    public ResponseDTO searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return response.suksesDTO(airplaneRepository.searchAll(updatedCode, updatedName, pageable));
    }

    public ResponseDTO save(AirplaneEntityDTO airplane) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            Airplane convertToairplane = modelMapper.map(airplane, Airplane.class);

            Optional<Company> checkCompanyData = companyRepository.findById(airplane.getCompanyId());
            if(checkCompanyData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            convertToairplane.setCompany(checkCompanyData.get());

            Airplane result = airplaneRepository.save(convertToairplane);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Airplane> checkData= airplaneRepository.findById(id);
        if (checkData.isEmpty()){
            return response.errorDTO(404, Config.DATA_NOT_FOUND);
        }else{
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, AirplaneEntityDTO airplane) {
        try{
            Optional<Airplane> checkData = airplaneRepository.findById(id);
            if(checkData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Airplane updatedAirplane = checkData.get();

            if(airplane.getName() != null){
                updatedAirplane.setName(airplane.getName());
            }
            if(airplane.getCode() != null){
                updatedAirplane.setCode(airplane.getCode());
            }
            if(airplane.getCompanyId() != null){
                Optional<Company> checkCompanyData = companyRepository.findById(airplane.getCompanyId());
                if(checkCompanyData.isEmpty()){
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                updatedAirplane.setCompany(checkCompanyData.get());
            }

            return response.suksesDTO(airplaneRepository.save(updatedAirplane));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try{
            Optional<Airplane> checkData = airplaneRepository.findById(id);
            if(checkData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Airplane deletedAirplane = checkData.get();
            deletedAirplane.setDeletedDate(new Date());
            return response.suksesDTO(airplaneRepository.save(deletedAirplane));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }
}
