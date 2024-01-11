package com.example.finalProject.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.finalProject.dto.ResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.finalProject.dto.AirportEntityDTO;
import com.example.finalProject.entity.Airport;
import com.example.finalProject.repository.AirportRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;

@Service
public class AirportImpl {
    @Autowired
    Response response;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    AirportRepository airportsRepository;

    public ResponseDTO searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return response.suksesDTO(airportsRepository.searchAll(updatedCode, updatedName, pageable));
    }

    public ResponseDTO save(AirportEntityDTO airport) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            Airport convertToairport = modelMapper.map(airport, Airport.class);
            Airport result = airportsRepository.save(convertToairport);

            return response.suksesDTO(result);
        } catch (Exception e) {
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Airport> checkData = airportsRepository.findById(id);
        if (checkData.isEmpty()) {
            return response.errorDTO(404, Config.DATA_NOT_FOUND);
        } else {
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, AirportEntityDTO airports) {
        try {
            Optional<Airport> checkData = airportsRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Airport updatedAirport = checkData.get();

            if (airports.getName() != null) {
                updatedAirport.setName(airports.getName());
            }
            if (airports.getCode() != null) {
                updatedAirport.setCode(airports.getCode());
            }
            if (airports.getCity() != null) {
                updatedAirport.setCity(airports.getCity());
            }
            if (airports.getCountry() != null) {
                updatedAirport.setCountry(airports.getCountry());
            }

            // Save the updated airport
            Airport savedAirport = airportsRepository.save(updatedAirport);

            return response.suksesDTO(savedAirport);
        } catch (Exception e) {
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try {
            Optional<Airport> checkData = airportsRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            Airport deletedAirports = checkData.get();
            deletedAirports.setDeletedDate(new Date());
            return response.suksesDTO(airportsRepository.save(deletedAirports));
        } catch (Exception e) {
            return response.errorDTO(404, e.getMessage());
        }
    }
}
