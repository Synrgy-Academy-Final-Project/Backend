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

import com.example.finalProject.DTO.AirportsEntityDTO;
import com.example.finalProject.entity.Airports;
import com.example.finalProject.repository.AirportsRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;

@Service
public class AirportsImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    AirportsRepository airportsRepository;

    public Page<Airports> searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return airportsRepository.searchAll(updatedCode, updatedName, pageable);
    }

    public Map<String, Object> save(AirportsEntityDTO airplane) {
        Map<String, Object> map = new HashMap<>();

        try {
            ModelMapper modelMapper = new ModelMapper();
            Airports convertToairplane = modelMapper.map(airplane, Airports.class);
            Airports result = airportsRepository.save(convertToairplane);

            map = response.sukses(result);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> findById(UUID id) {
        Map<String, Object> map;

        Optional<Airports> checkData = airportsRepository.findById(id);
        if (checkData.isEmpty()) {
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        } else {
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map<String, Object> update(UUID id, AirportsEntityDTO airports) {
        Map<String, Object> map;
        try {
            Optional<Airports> checkData = airportsRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Airports updatedAirports = checkData.get();

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
            Optional<Airports> checkData = airportsRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            Airports deletedAirports = checkData.get();
            deletedAirports.setDeletedDate(new Date());
            map = response.sukses(airportsRepository.save(deletedAirports));
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
