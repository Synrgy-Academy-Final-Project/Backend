package com.example.finalProject.service.basepriceDate;

import com.example.finalProject.dto.BasepriceDateDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.BasePriceDate;
import com.example.finalProject.repository.BasepriceDateRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasepriceDateServiceImpl implements BasepriceDateService{
    private final BasepriceDateRepository basepriceDateRepository;
    private final Response response;
    private final GeneralFunction generalFunction;

    @Transactional
    @Override
    public ResponseDTO save(BasepriceDateDTO request){
        try{
            ModelMapper modelMapper = new ModelMapper();
            BasePriceDate map = modelMapper.map(request, BasePriceDate.class);
            BasePriceDate result = basepriceDateRepository.save(map);

            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Override
    public ResponseDTO findById(UUID id){
        Optional<BasePriceDate> checkData = basepriceDateRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("BasepriceDate");
        }else {
            return response.suksesDTO(checkData.get());
        }
    }

    @Transactional
    @Override
    public ResponseDTO update(UUID id, BasepriceDateDTO request){
        try {
            Optional<BasePriceDate> checkData = basepriceDateRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("BasepriceDate");
            }

            BasePriceDate updateBasePriceDate = checkData.get();

            if (request.getDateFrom() != null){
                updateBasePriceDate.setDateFrom(request.getDateFrom());
            }
            if (request.getDateTo() != null){
                updateBasePriceDate.setDateTo(request.getDateTo());
            }
            if (request.getType() != null){
                updateBasePriceDate.setType(request.getType());
            }
            if (request.getDatePrice() != null){
                updateBasePriceDate.setDatePrice(request.getDatePrice());
            }

            BasePriceDate save = basepriceDateRepository.save(updateBasePriceDate);
            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDTO delete(UUID id) {
        try {
            Optional<BasePriceDate> checkData = basepriceDateRepository.findById(id);
            if (checkData.isEmpty()){
                return response.dataNotFound("BasepriceDate");
            }

            BasePriceDate deletedBasePriceDate = checkData.get();
            deletedBasePriceDate.setDeletedDate(new Date());

            BasePriceDate save = basepriceDateRepository.save(deletedBasePriceDate);
            return response.suksesDTO(save);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    @Override
    public ResponseDTO searchAll(String dateFrom1, String dateFrom2, String dateTo1, String dateTo2, String priceDown, String priceUp, String type, Pageable pageable) {
        Page<BasePriceDate> result = basepriceDateRepository.searchAll(dateFrom1, dateFrom2, dateTo1, dateTo2, priceDown, priceUp, type, pageable);
        return response.suksesDTO(result);
    }
}
