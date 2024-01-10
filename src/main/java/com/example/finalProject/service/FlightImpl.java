package com.example.finalProject.service;

import com.example.finalProject.dto.FlightEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
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

    public ResponseDTO searchAll(String fromAirportCode, String toAirportCode, Date departureDate, int capacity, String airplaneClass, Pageable pageable) {
        String updatedFromAirportCode = generalFunction.createLikeQuery(fromAirportCode);
        String updatedToAirportCode = generalFunction.createLikeQuery(toAirportCode);
        String updatedAirplaneClass = generalFunction.createLikeQuery(airplaneClass);

        return response.suksesDTO(flightRepository.searchAll(updatedFromAirportCode, updatedToAirportCode, departureDate, updatedAirplaneClass, capacity, pageable));
    }

    public ResponseDTO save(FlightEntityDTO flight) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            Flight convertToFlight = modelMapper.map(flight, Flight.class);

            Optional<Airplane> checkAirplane = airplaneRepository.findById(flight.getAirplaneId());
            if(checkAirplane.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            convertToFlight.setAirplane(checkAirplane.get());

            Optional<Airport> checkFromAirport = airportRepository.findById(flight.getFromAirportId());
            if(checkFromAirport.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            convertToFlight.setFromAirport(checkFromAirport.get());

            Optional<Airport> checkToAirport = airportRepository.findById(flight.getToAirportId());
            if(checkToAirport.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }
            convertToFlight.setToAirport(checkToAirport.get());

            Flight result = flightRepository.save(convertToFlight);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Flight> checkData= flightRepository.findById(id);
        if (checkData.isEmpty()){
            return response.errorDTO(404, Config.DATA_NOT_FOUND);
        }else{
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, FlightEntityDTO flight) {
        try{
            Optional<Flight> checkData = flightRepository.findById(id);
            if(checkData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Flight updatedFlight = checkData.get();

            if(flight.getAirplaneId() != null){
                Optional<Airplane> checkAirplaneData = airplaneRepository.findById(flight.getAirplaneId());
                if(checkAirplaneData.isEmpty()){
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
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
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                updatedFlight.setFromAirport(checkAirportData.get());
            }

            if(flight.getToAirportId() != null){
                Optional<Airport> checkAirportData = airportRepository.findById(flight.getToAirportId());
                if(checkAirportData.isEmpty()){
                    return response.errorDTO(404, Config.DATA_NOT_FOUND);
                }
                updatedFlight.setToAirport(checkAirportData.get());
            }

            if(flight.getPrice() != null){
                updatedFlight.setPrice(flight.getPrice());
            }

            return response.suksesDTO(flightRepository.save(updatedFlight));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try{
            Optional<Flight> checkData = flightRepository.findById(id);
            if(checkData.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            Flight deletedFlight = checkData.get();
            deletedFlight.setDeletedDate(new Date());
            return response.suksesDTO(flightRepository.save(deletedFlight));
        }catch (Exception e){
            return response.errorDTO(404, e.getMessage());
        }
    }
}
