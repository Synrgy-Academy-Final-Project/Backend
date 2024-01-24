package com.example.finalProject.service;

import com.example.finalProject.dto.AirplaneEntityDTO;
import com.example.finalProject.dto.AirplaneListDTO;
import com.example.finalProject.dto.AirplaneListRequestDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.*;
import com.example.finalProject.repository.AirplaneRepository;
import com.example.finalProject.repository.BasepriceAirportRepository;
import com.example.finalProject.repository.BasepriceDateRepository;
import com.example.finalProject.repository.CompanyRepository;
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
public class AirplaneImpl {
    @Autowired
    Response response;
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    BasepriceAirportRepository basepriceAirportRepository;
    @Autowired
    BasepriceDateRepository basepriceDateRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    GeneralFunction generalFunction;
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    private ModelMapper modelMapper = new ModelMapper();

    public ResponseDTO searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return response.suksesDTO(airplaneRepository.searchAll(updatedCode, updatedName, pageable));
    }

    public ResponseDTO airplaneList(AirplaneListRequestDTO airplaneList, Pageable pageable) {
        Page<Airplane> data = airplaneRepository.airplaneList(airplaneList.getAirplaneClass(), airplaneList.getCapacity(), generalFunction.createLikeQuery(airplaneList.getMaskapai()), pageable);
        List<AirplaneListDTO> result = new ArrayList<>();
        int airportPrice = 0;
        int datePrice = 0;
        BasePriceAirport airportData = basepriceAirportRepository.getAirportPrice(airplaneList.getFromAirport(), airplaneList.getToAirport());
        BasePriceDate dateData = basepriceDateRepository.getDatePrice(airplaneList.getDepartureDate());
        System.out.println(dateData);
        if (airportData != null){
            airportPrice = airportData.getAirportPrice();
        }
        if(dateData != null){
            datePrice = dateData.getDatePrice();
        }

        try {
            for (Airplane airplane : data) {
                for (AirplaneClass airplaneClass : airplane.getAirplaneClass()) {
                    for (AirplaneFlightTime airplaneTime : airplane.getAirplaneFlightTimes()) {
                        AirplaneListDTO airplaneDTO = modelMapper.map(airplane, AirplaneListDTO.class);
                        airplaneDTO.setTotalPrice(airplane.getAirplanePrice() + airplaneTime.getAirplaneFlightTimePrice() + airplaneClass.getAirplaneClassPrice() + airportPrice + datePrice);
                        airplaneDTO.setAirplaneClass(airplaneClass);
                        airplaneDTO.setAirplaneFlightTimes(airplaneTime);
                        result.add(airplaneDTO);
                    }
                }
            }

            result.sort(Comparator.comparing(AirplaneListDTO::getTotalPrice));
//        result.sort(Comparator.comparing(AirplaneListDTO::getTotalPrice).reversed());

            result = result.stream().filter(airplane -> airplane.getTotalPrice() > airplaneList.getFromPrice()
                            && airplane.getTotalPrice() < airplaneList.getToPrice()
                            && airplane.getAirplaneFlightTimes().getFlightTime().after(airplaneList.getFromTime())
                            && airplane.getAirplaneFlightTimes().getFlightTime().before(airplaneList.getToTime())
                            && airplane.getAirplaneClass().getAirplaneClass().equals(airplaneList.getAirplaneClass()))
                    .toList();
            System.out.println(airplaneList);
            return response.suksesDTO(result);
        }catch(Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO save(AirplaneEntityDTO airplane) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            Airplane convertToairplane = modelMapper.map(airplane, Airplane.class);

            Optional<Company> checkCompanyData = companyRepository.findById(airplane.getCompanyId());
            if(checkCompanyData.isEmpty()){
                return response.dataNotFound("Company");
            }
            convertToairplane.setCompany(checkCompanyData.get());

            Airplane result = airplaneRepository.save(convertToairplane);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Airplane> checkData= airplaneRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("Airplane");
        }else{
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, AirplaneEntityDTO airplane) {
        try{
            Optional<Airplane> checkData = airplaneRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("Airplane");
            }

            Airplane updatedAirplane = checkData.get();

            if(airplane.getName() != null){
                updatedAirplane.setName(airplane.getName());
            }
            if(airplane.getCode() != null){
                updatedAirplane.setCode(airplane.getCode());
            }
            if(airplane.getAirplanePrice() != null){
                updatedAirplane.setAirplanePrice(airplane.getAirplanePrice());
            }
            if(airplane.getCompanyId() != null){
                Optional<Company> checkCompanyData = companyRepository.findById(airplane.getCompanyId());
                if(checkCompanyData.isEmpty()){
                    return response.dataNotFound("Company");
                }
                updatedAirplane.setCompany(checkCompanyData.get());
            }

            return response.suksesDTO(airplaneRepository.save(updatedAirplane));
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try{
            Optional<Airplane> checkData = airplaneRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("Airplane");
            }

            Airplane deletedAirplane = checkData.get();
            deletedAirplane.setDeletedDate(new Date());
            return response.suksesDTO(airplaneRepository.save(deletedAirplane));
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
