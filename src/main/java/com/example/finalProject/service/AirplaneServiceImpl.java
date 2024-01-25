package com.example.finalProject.service;

import com.example.finalProject.dto.*;
import com.example.finalProject.entity.*;
import com.example.finalProject.repository.*;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class AirplaneServiceImpl {
    @Autowired
    Response response;
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    BasepriceAirportRepository basepriceAirportRepository;
    @Autowired
    BasepriceDateRepository basepriceDateRepository;
    @Autowired
    AirplaneServiceRepository airplaneServiceRepository;
    @Autowired
    AirplaneClassRepository airplaneClassRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    GeneralFunction generalFunction;

    public ResponseDTO searchAll(Pageable pageable) {
        return response.suksesDTO(airplaneServiceRepository.findAll(pageable));
    }

    public ResponseDTO save(AirplaneServiceEntityDTO airplaneService) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            AirplaneService convertToairplaneService = modelMapper.map(airplaneService, AirplaneService.class);

            Optional<AirplaneClass> checkAirplaneClass = airplaneClassRepository.findById(airplaneService.getAirplaneClassId());
            if(checkAirplaneClass.isEmpty()){
                return response.dataNotFound("AirplaneClass");
            }
            convertToairplaneService.setAirplaneClass(checkAirplaneClass.get());

            AirplaneService result = airplaneServiceRepository.save(convertToairplaneService);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<AirplaneService> checkData= airplaneServiceRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("AirplaneService");
        }else{
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, AirplaneServiceEntityDTO airplaneService) {
        try{
            Optional<AirplaneService> checkData = airplaneServiceRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("AirplaneService");
            }

            AirplaneService updatedAirplaneService = checkData.get();

            if(airplaneService.getBaggage() != null){
                updatedAirplaneService.setBaggage(airplaneService.getBaggage());
            }
            if(airplaneService.getCabinBaggage() != null){
                updatedAirplaneService.setCabinBaggage(airplaneService.getCabinBaggage());
            }
            if(airplaneService.getMeals() != null){
                updatedAirplaneService.setMeals(airplaneService.getMeals());
            }
            if(airplaneService.getTravelInsurance() != null){
                updatedAirplaneService.setTravelInsurance(airplaneService.getTravelInsurance());
            }
            if(airplaneService.getInflightEntertainment() != null){
                updatedAirplaneService.setInflightEntertainment(airplaneService.getInflightEntertainment());
            }
            if(airplaneService.getElectricSocket() != null){
                updatedAirplaneService.setElectricSocket(airplaneService.getElectricSocket());
            }
            if(airplaneService.getWifi() != null){
                updatedAirplaneService.setWifi(airplaneService.getWifi());
            }
            if(airplaneService.getReschedule() != null){
                updatedAirplaneService.setReschedule(airplaneService.getReschedule());
            }
            if(airplaneService.getRefund() != null){
                updatedAirplaneService.setRefund(airplaneService.getRefund());
            }

            return response.suksesDTO(airplaneServiceRepository.save(updatedAirplaneService));
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try{
            Optional<AirplaneService> checkData = airplaneServiceRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("AirplaneService");
            }

            AirplaneService deletedAirplaneService = checkData.get();
            deletedAirplaneService.setDeletedDate(new Date());
            return response.suksesDTO(airplaneServiceRepository.save(deletedAirplaneService));
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO minimumPrice(String fromAirportCode, String toAirportCode, Date departureDate) {
        List<Map<String, Object>> sevenDays = new ArrayList<>();
        LocalDate date = LocalDate.ofInstant(departureDate.toInstant(), ZoneId.systemDefault());
        for (int i = 0; i < 7; i++){
            Map<String, Object> data = new HashMap<>();
            LocalDate theDate = date.plusDays(i);
            data.put("date", theDate);
            data.put("price", airplaneRepository.getMinimumPriceThatDay(fromAirportCode, toAirportCode, theDate));
            sevenDays.add(data);
        }
        return response.suksesDTO(sevenDays);
    }
}
