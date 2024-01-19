package com.example.finalProject.service.airplaneFlightTime;

import com.example.finalProject.dto.AirplaneFlightTimeDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.AirplaneFlightTime;
import com.example.finalProject.repository.AirplaneFlightTimeRepository;
import com.example.finalProject.repository.AirplaneRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AirplaneFlightTimeServiceImpl implements AirplaneFlightTimeService {
    private final AirplaneFlightTimeRepository airplaneFlightTimeRepository;
    private final AirplaneRepository airplaneRepository;
    private final Response response;
    private final GeneralFunction generalFunction;

    @Transactional
    @Override
    public ResponseDTO save(AirplaneFlightTimeDTO request){
        try{
            ModelMapper modelMapper = new ModelMapper();
            AirplaneFlightTime map = modelMapper.map(request, AirplaneFlightTime.class);

            Optional<Airplane> checkAirplaneData = airplaneRepository.findById(request.getAirplaneId());
            if (checkAirplaneData.isEmpty()){
                return response.dataNotFound("Airplane");
            }


            map.setAirplane(checkAirplaneData.get());
            map.setFlightTime(Time.valueOf(request.getFlightTime()));

            AirplaneFlightTime result = airplaneFlightTimeRepository.save(map);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Override
    public ResponseDTO findById(UUID id){
        Optional<AirplaneFlightTime> checkData = airplaneFlightTimeRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("Airplane Flight Time");
        }else {
            return response.suksesDTO(checkData.get());
        }
    }

    @Transactional
    @Override
    public ResponseDTO update(UUID id, AirplaneFlightTimeDTO request){
        try {
            Optional<AirplaneFlightTime> checkData = airplaneFlightTimeRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("Airplane Class");
            }

            AirplaneFlightTime updateAirplaneFlightTime = checkData.get();

            if (request.getAirplaneId() != null){
                Optional<Airplane> checkAirplaneData = airplaneRepository.findById(request.getAirplaneId());
                if (checkAirplaneData.isEmpty()){
                    return response.dataNotFound("Airplane");
                }
                updateAirplaneFlightTime.setAirplane(checkAirplaneData.get());
            }

            if (request.getFlightTime() != null){
                updateAirplaneFlightTime.setFlightTime(Time.valueOf(request.getFlightTime()));
            }

            if (request.getAirplaneFlightTimePrice() != null){
                updateAirplaneFlightTime.setAirplaneFlightTimePrice(request.getAirplaneFlightTimePrice());
            }

            AirplaneFlightTime save = airplaneFlightTimeRepository.save(updateAirplaneFlightTime);
            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDTO delete(UUID id) {
        try {
            Optional<AirplaneFlightTime> checkData = airplaneFlightTimeRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("Airplane Class");
            }

            AirplaneFlightTime deletedAirplaneFlightTime = checkData.get();
            deletedAirplaneFlightTime.setDeletedDate(new Date());

            AirplaneFlightTime save = airplaneFlightTimeRepository.save(deletedAirplaneFlightTime);

            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Override
    public ResponseDTO searchAll(String airplaneClass, String airplanePrice, Pageable pageable) {
//        String likeQueryAirplaneFlightTime = generalFunction.createLikeQuery(airplaneClass);
//        String likeQueryAiplanePrice = generalFunction.createLikeQuery(airplanePrice);
//        Page<BasePriceAirplane> result = airplaneFlightTimeRepository.searchAll(likeQueryAirplaneFlightTime, likeQueryAiplanePrice, pageable);
//        return response.suksesDTO(result);
        return null;
    }
}

