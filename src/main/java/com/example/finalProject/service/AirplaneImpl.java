package com.example.finalProject.service;

import com.example.finalProject.DTO.AirplaneEntityDTO;
import com.example.finalProject.DTO.CompanyEntityDTO;
import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.Company;
import com.example.finalProject.repository.AirplaneRepository;
import com.example.finalProject.repository.CompanyRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirplaneImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    GeneralFunction generalFunction;

    public Page<Airplane> searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return airplaneRepository.searchAll(updatedCode, updatedName, pageable);
    }

    public Map save(AirplaneEntityDTO airplane) {
        Map map = new HashMap<>();

        try{
            ModelMapper modelMapper = new ModelMapper();
            Airplane convertToairplane = modelMapper.map(airplane, Airplane.class);

            Optional<Company> checkCompanyData = companyRepository.findById(airplane.getCompanyId());
            if(checkCompanyData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            convertToairplane.setCompany(checkCompanyData.get());

            Airplane result = airplaneRepository.save(convertToairplane);

            map = response.sukses(result);
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map findById(UUID id) {
        Map map;

        Optional<Airplane> checkData= airplaneRepository.findById(id);
        if (checkData.isEmpty()){
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        }else{
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map update(UUID id, AirplaneEntityDTO airplane) {
        Map map;
        try{
            Optional<Airplane> checkData = airplaneRepository.findById(id);
            if(checkData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
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
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                updatedAirplane.setCompany(checkCompanyData.get());
            }

            map = response.sukses(airplaneRepository.save(updatedAirplane));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map delete(UUID id) {
        Map map;
        try{
            Optional<Airplane> checkData = airplaneRepository.findById(id);
            if(checkData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Airplane deletedAirplane = checkData.get();
            deletedAirplane.setDeletedDate(new Date());
            map = response.sukses(airplaneRepository.save(deletedAirplane));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
