package com.example.finalProject.service.airplaneClass;

import com.example.finalProject.dto.AirplaneClassDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Airplane;
import com.example.finalProject.entity.AirplaneClass;
import com.example.finalProject.repository.AirplaneClassRepository;
import com.example.finalProject.repository.AirplaneRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AirplaneClassServiceImpl implements AirplaneClassService {
    private final AirplaneClassRepository airplaneClassRepository;
    private final AirplaneRepository airplaneRepository;
    private final Response response;
    private final GeneralFunction generalFunction;

    @Transactional
    @Override
    public ResponseDTO save(AirplaneClassDTO request){
        try{
            ModelMapper modelMapper = new ModelMapper();
            AirplaneClass map = modelMapper.map(request, AirplaneClass.class);

            Optional<Airplane> checkAirplaneData = airplaneRepository.findById(request.getAirplaneId());
            if (checkAirplaneData.isEmpty()){
                return response.dataNotFound("Airplane");
            }
            map.setAirplane(checkAirplaneData.get());

            AirplaneClass result = airplaneClassRepository.save(map);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Override
    public ResponseDTO findById(UUID id){
        Optional<AirplaneClass> checkData = airplaneClassRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("Airplane Class");
        }else {
            return response.suksesDTO(checkData.get());
        }
    }

    @Transactional
    @Override
    public ResponseDTO update(UUID id, AirplaneClassDTO request){
        try {
            Optional<AirplaneClass> checkData = airplaneClassRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("Airplane Class");
            }

            AirplaneClass updateAirplaneClass = checkData.get();

            if (request.getAirplaneId() != null){
                Optional<Airplane> checkAirplaneData = airplaneRepository.findById(request.getAirplaneId());
                if (checkAirplaneData.isEmpty()){
                    return response.dataNotFound("Airplane");
                }
                updateAirplaneClass.setAirplane(checkAirplaneData.get());
            }

            if (request.getAirplaneClass() != null){
                updateAirplaneClass.setAirplaneClass(request.getAirplaneClass());
            }

            if (request.getAirplaneClassPrice() != null){
                updateAirplaneClass.setAirplaneClassPrice(request.getAirplaneClassPrice());
            }

            AirplaneClass save = airplaneClassRepository.save(updateAirplaneClass);
            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDTO delete(UUID id) {
        try {
            Optional<AirplaneClass> checkData = airplaneClassRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("Airplane Class");
            }

            AirplaneClass deletedAirplaneClass = checkData.get();
            deletedAirplaneClass.setDeletedDate(new Date());

            AirplaneClass save = airplaneClassRepository.save(deletedAirplaneClass);

            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Override
    public ResponseDTO searchAll(String airplaneClass, String airplanePrice, Pageable pageable) {
//        String likeQueryAirplaneClass = generalFunction.createLikeQuery(airplaneClass);
//        String likeQueryAiplanePrice = generalFunction.createLikeQuery(airplanePrice);
//        Page<BasePriceAirplane> result = airplaneClassRepository.searchAll(likeQueryAirplaneClass, likeQueryAiplanePrice, pageable);
//        return response.suksesDTO(result);
        return null;
    }
}

