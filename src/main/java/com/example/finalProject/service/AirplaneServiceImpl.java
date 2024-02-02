package com.example.finalProject.service;

import com.example.finalProject.dto.AirplaneServiceEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.AirplaneClass;
import com.example.finalProject.entity.AirplaneService;
import com.example.finalProject.repository.AirplaneClassRepository;
import com.example.finalProject.repository.AirplaneServiceRepository;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AirplaneServiceImpl {
    @Autowired
    Response response;
    @Autowired
    AirplaneServiceRepository airplaneServiceRepository;
    @Autowired
    AirplaneClassRepository airplaneClassRepository;

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
}
