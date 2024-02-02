package com.example.finalProject.service;

import com.example.finalProject.dto.AirplaneAdditionalServiceDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.AirplaneAdditionalService;
import com.example.finalProject.repository.AirplaneAdditionalServiceRepository;
import com.example.finalProject.repository.AirplaneRepository;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AirplaneAdditionalServiceImpl {
    @Autowired
    Response response;
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    AirplaneAdditionalServiceRepository airplaneAdditionalServiceRepository;

    public ResponseDTO searchAll(Pageable pageable) {
        return response.suksesDTO(airplaneAdditionalServiceRepository.findAll(pageable));
    }

    public ResponseDTO save(AirplaneAdditionalServiceDTO airplaneAdditionalService) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            AirplaneAdditionalService convertToairplaneAdditionalService = modelMapper.map(airplaneAdditionalService, AirplaneAdditionalService.class);

            Optional<Airplane> checkAirplane = airplaneRepository.findById(airplaneAdditionalService.getAirplaneId());
            if(checkAirplane.isEmpty()){
                return response.dataNotFound("Airplane");
            }
            convertToairplaneAdditionalService.setAirplane(checkAirplane.get());

            AirplaneAdditionalService result = airplaneAdditionalServiceRepository.save(convertToairplaneAdditionalService);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<AirplaneAdditionalService> checkData= airplaneAdditionalServiceRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("AirplaneAdditionalService");
        }else{
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, AirplaneAdditionalServiceDTO airplaneAdditionalService) {
        try{
            Optional<AirplaneAdditionalService> checkData = airplaneAdditionalServiceRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("AirplaneAdditionalService");
            }

            AirplaneAdditionalService updatedAirplaneAdditionalService = checkData.get();

            if(airplaneAdditionalService.getAirplaneId() != null){
                Optional<Airplane> checkAirplaneData = airplaneRepository.findById(airplaneAdditionalService.getAirplaneId());
                if(checkAirplaneData.isEmpty()){
                    return response.dataNotFound("Airplane");
                }
                updatedAirplaneAdditionalService.setAirplane(checkAirplaneData.get());
            }

            if(airplaneAdditionalService.getType() != null){
                updatedAirplaneAdditionalService.setType(airplaneAdditionalService.getType());
            }
            if(airplaneAdditionalService.getQuantity() != null){
                updatedAirplaneAdditionalService.setQuantity(airplaneAdditionalService.getQuantity());
            }
            if(airplaneAdditionalService.getPrice() != null){
                updatedAirplaneAdditionalService.setPrice(airplaneAdditionalService.getPrice());
            }

            return response.suksesDTO(airplaneAdditionalServiceRepository.save(updatedAirplaneAdditionalService));
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try{
            Optional<AirplaneAdditionalService> checkData = airplaneAdditionalServiceRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("AirplaneAdditionalService");
            }

            AirplaneAdditionalService deletedAirplaneAdditionalService = checkData.get();
            deletedAirplaneAdditionalService.setDeletedDate(new Date());
            return response.suksesDTO(airplaneAdditionalServiceRepository.save(deletedAirplaneAdditionalService));
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }
}
