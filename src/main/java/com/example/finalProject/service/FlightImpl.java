package com.example.finalProject.service;

import com.example.finalProject.dto.FlightEntityDTO;
import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.Airport;
import com.example.finalProject.entity.Flight;
import com.example.finalProject.repository.AirplaneRepository;
import com.example.finalProject.repository.AirportRepository;
import com.example.finalProject.repository.FlightRepository;
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
public class FlightImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    AirplaneRepository airplaneRepository;
    @Autowired
    AirportRepository airportRepository;
    @Autowired
    GeneralFunction generalFunction;

    public Page<Flight> searchAll(String fromAirportCode, String toAirportCode, Date departureDate, Date arrivalDate, int capacity, String airplaneClass, Pageable pageable) {
        String updatedFromAirportCode = generalFunction.createLikeQuery(fromAirportCode);
        String updatedToAirportCode = generalFunction.createLikeQuery(toAirportCode);
        String updatedAirplaneClass = generalFunction.createLikeQuery(airplaneClass);

        return flightRepository.searchAll(updatedFromAirportCode, updatedToAirportCode, departureDate, arrivalDate, updatedAirplaneClass, capacity, pageable);
    }

    public Map save(FlightEntityDTO flight) {
        Map map = new HashMap<>();

        try{
            ModelMapper modelMapper = new ModelMapper();
            Flight convertToFlight = modelMapper.map(flight, Flight.class);

            Optional<Airplane> checkAirplane = airplaneRepository.findById(flight.getAirplaneId());
            if(checkAirplane.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            convertToFlight.setAirplane(checkAirplane.get());

            Optional<Airport> checkFromAirport = airportRepository.findById(flight.getFromAirportId());
            if(checkFromAirport.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            convertToFlight.setFromAirport(checkFromAirport.get());

            Optional<Airport> checkToAirport = airportRepository.findById(flight.getToAirportId());
            if(checkToAirport.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            convertToFlight.setToAirport(checkToAirport.get());

            Flight result = flightRepository.save(convertToFlight);

            map = response.sukses(result);
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map findById(UUID id) {
        Map map;

        Optional<Flight> checkData= flightRepository.findById(id);
        if (checkData.isEmpty()){
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        }else{
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map update(UUID id, FlightEntityDTO flight) {
        Map map;
        try{
            Optional<Flight> checkData = flightRepository.findById(id);
            if(checkData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Flight updatedFlight = checkData.get();

            if(flight.getAirplaneId() != null){
                Optional<Airplane> checkAirplaneData = airplaneRepository.findById(flight.getAirplaneId());
                if(checkAirplaneData.isEmpty()){
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                updatedFlight.setAirplane(checkAirplaneData.get());
            }

            if(flight.getDepartureDate() != null){
                updatedFlight.setDepartureDate(flight.getDepartureDate());
            }

            if(flight.getArrivalDate() != null){
                updatedFlight.setArrivalDate(flight.getArrivalDate());
            }

            if(flight.getCapacity() != null){
                updatedFlight.setCapacity(flight.getCapacity());
            }

            if(flight.getAirplaneClass() != null){
                updatedFlight.setAirplaneClass(flight.getAirplaneClass());
            }

            if(flight.getFromAirportId() != null){
                Optional<Airport> checkAirportData = airportRepository.findById(flight.getFromAirportId());
                if(checkAirportData.isEmpty()){
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                updatedFlight.setFromAirport(checkAirportData.get());
            }

            if(flight.getToAirportId() != null){
                Optional<Airport> checkAirportData = airportRepository.findById(flight.getToAirportId());
                if(checkAirportData.isEmpty()){
                    return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
                }
                updatedFlight.setToAirport(checkAirportData.get());
            }

            if(flight.getPrice() != null){
                updatedFlight.setPrice(flight.getPrice());
            }

            map = response.sukses(flightRepository.save(updatedFlight));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map delete(UUID id) {
        Map map;
        try{
            Optional<Flight> checkData = flightRepository.findById(id);
            if(checkData.isEmpty()){
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Flight deletedFlight = checkData.get();
            deletedFlight.setDeletedDate(new Date());
            map = response.sukses(flightRepository.save(deletedFlight));
        }catch (Exception e){
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
