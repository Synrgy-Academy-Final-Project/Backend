package com.example.finalProject.service;

import com.example.finalProject.dto.PromotionEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Promotion;
import com.example.finalProject.repository.PromotionRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PromotionImpl {
    @Autowired
    Response response;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    PromotionRepository promotionRepository;

    public ResponseDTO searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return response.suksesDTO(promotionRepository.searchAll(updatedCode, updatedName, pageable));
    }

    public ResponseDTO save(PromotionEntityDTO promotion) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            Promotion convertTopromotion = modelMapper.map(promotion, Promotion.class);
            Promotion result = promotionRepository.save(convertTopromotion);

            return response.suksesDTO(result);
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findByCode(String code) {
        Optional<Promotion> checkData = promotionRepository.findPromotionByCode(code);
        if (checkData.isEmpty()) {
            return response.dataNotFound("Promotion");
        } else {
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, PromotionEntityDTO promotion) {
        try {
            Optional<Promotion> checkData = promotionRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.dataNotFound("Promotion");
            }

            Promotion updatedPromotion = checkData.get();

            if (promotion.getTitle() != null) {
                updatedPromotion.setTitle(promotion.getTitle());
            }
            if (promotion.getDescription() != null) {
                updatedPromotion.setDescription(promotion.getDescription());
            }
            if (promotion.getCode() != null) {
                updatedPromotion.setCode(promotion.getCode());
            }
            if (promotion.getDiscount() != null) {
                updatedPromotion.setDiscount(promotion.getDiscount());
            }
            if (promotion.getTerms() != null) {
                updatedPromotion.setTerms(promotion.getTerms());
            }
            if (promotion.getStartDate() != null) {
                updatedPromotion.setStartDate(promotion.getStartDate());
            }
            if (promotion.getEndDate() != null) {
                updatedPromotion.setEndDate(promotion.getEndDate());
            }

            // Save the updated promotion
            Promotion savedPromotion = promotionRepository.save(updatedPromotion);

            return response.suksesDTO(savedPromotion);
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try {
            Optional<Promotion> checkData = promotionRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.dataNotFound("Promotion");
            }
            Promotion deletedPromotion = checkData.get();
            deletedPromotion.setDeletedDate(new Date());
            return response.suksesDTO(promotionRepository.save(deletedPromotion));
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }
}
