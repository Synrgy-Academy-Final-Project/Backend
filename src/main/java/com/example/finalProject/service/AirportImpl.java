package com.example.finalProject.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    Config config;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    AirportRepository airportsRepository;

    public Page<Airport> searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return airportsRepository.searchAll(updatedCode, updatedName, pageable);
    }

    public Map<String, Object> save(AirportEntityDTO airplane) {
        Map<String, Object> map = new HashMap<>();

        try {
            ModelMapper modelMapper = new ModelMapper();
            Airport convertToairplane = modelMapper.map(airplane, Airport.class);
            Airport result = airportsRepository.save(convertToairplane);

            map = response.sukses(result);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> findById(UUID id) {
        Map<String, Object> map;

        Optional<Airport> checkData = airportsRepository.findById(id);
        if (checkData.isEmpty()) {
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        } else {
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map<String, Object> update(UUID id, AirportEntityDTO airports) {
        Map<String, Object> map;
        try {
            Optional<Airport> checkData = airportsRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Airport updatedAirports = checkData.get();

            if (airports.getName() != null) {
                updatedAirports.setName(airports.getName());
            }
            if (airports.getCode() != null) {
                updatedAirports.setCode(airports.getCode());
            }

            map = response.sukses(airportsRepository.save(updatedAirports));
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> delete(UUID id) {
        Map<String, Object> map;
        try {
            Optional<Airport> checkData = airportsRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            Airport deletedAirports = checkData.get();
            deletedAirports.setDeletedDate(new Date());
            map = response.sukses(airportsRepository.save(deletedAirports));
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
