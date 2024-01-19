package com.example.finalProject.service.basepriceAirport;

import com.example.finalProject.dto.BasepriceAirportDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Airport;
import com.example.finalProject.entity.BasePriceAirport;
import com.example.finalProject.repository.AirportRepository;
import com.example.finalProject.repository.BasepriceAirportRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasepriceAirportServiceImpl implements BasepriceAirportService {
    private final BasepriceAirportRepository basepriceAirportRepository;
    private final AirportRepository airportRepository;
    private final Response response;
    private final GeneralFunction generalFunction;

    @Transactional
    @Override
    public ResponseDTO save(BasepriceAirportDTO request){
        try{
            ModelMapper modelMapper = new ModelMapper();
            BasePriceAirport map = modelMapper.map(request, BasePriceAirport.class);

            Optional<Airport> checkAirportFrom = airportRepository.findById(request.getFromAirport());
            if (checkAirportFrom.isEmpty()){
                return response.dataNotFound("Airport");
            }
            map.setFromAirport(checkAirportFrom.get());
            map.setDepartureCode(checkAirportFrom.get().getCode());

            Optional<Airport> checkAirportTo = airportRepository.findById(request.getToAirport());
            if (checkAirportTo.isEmpty()){
                return response.dataNotFound("Airport");
            }
            map.setToAirport(checkAirportTo.get());
            map.setArrivalCode(checkAirportTo.get().getCode());

            BasePriceAirport result = basepriceAirportRepository.save(map);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Override
    public ResponseDTO findById(UUID id){
        Optional<BasePriceAirport>  checkData = basepriceAirportRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("BasepriceAirplane");
        }else {
            return response.suksesDTO(checkData.get());
        }
    }

    @Transactional
    @Override
    public ResponseDTO update(UUID id, BasepriceAirportDTO request){
        try {
            Optional<BasePriceAirport>  checkData = basepriceAirportRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("BasepriceAirplane");
            }

            BasePriceAirport updateBasepriceAirportDTO = checkData.get();

            if (request.getFromAirport() != null){
                Optional<Airport> checkAirportFrom = airportRepository.findById(request.getFromAirport());
                if (checkAirportFrom.isEmpty()){
                    return response.dataNotFound("Airport");
                }
                updateBasepriceAirportDTO.setFromAirport(checkAirportFrom.get());
                updateBasepriceAirportDTO.setDepartureCode(checkAirportFrom.get().getCode());
            }
            if (request.getFromAirport() != null){
                Optional<Airport> checkAirportTo = airportRepository.findById(request.getToAirport());
                if (checkAirportTo.isEmpty()){
                    return response.dataNotFound("Airport");
                }
                updateBasepriceAirportDTO.setToAirport(checkAirportTo.get());
                updateBasepriceAirportDTO.setArrivalCode(checkAirportTo.get().getCode());
            }
            
            if (request.getAirportPrice() != null){
                updateBasepriceAirportDTO.setAirportPrice(request.getAirportPrice());
            }

            BasePriceAirport save = basepriceAirportRepository.save(updateBasepriceAirportDTO);
            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDTO delete(UUID id) {
        try {
            Optional<BasePriceAirport>  checkData = basepriceAirportRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("BasepriceAirplane");
            }

            BasePriceAirport deletedBasepriceAirportDTO = checkData.get();
            deletedBasepriceAirportDTO.setDeletedDate(new Date());

            BasePriceAirport save = basepriceAirportRepository.save(deletedBasepriceAirportDTO);
            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

//    public ResponseDTO searchAll(String airplaneClass, String airplanePrice, Pageable pageable) {
//        String likeQueryAirplaneClass = generalFunction.createLikeQuery(airplaneClass);
//        String likeQueryAiplanePrice = generalFunction.createLikeQuery(airplanePrice);
//        Page<BasepriceAirportDTO> result = basepriceAirportRepository.searchAll(likeQueryAirplaneClass, likeQueryAiplanePrice, pageable);
//        return response.suksesDTO(result);
//    }
}

