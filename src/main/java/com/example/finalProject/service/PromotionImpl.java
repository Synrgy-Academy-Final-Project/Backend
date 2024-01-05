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

import com.example.finalProject.dto.PromotionEntityDTO;
import com.example.finalProject.entity.Promotion;
import com.example.finalProject.repository.PromotionRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;

@Service
public class PromotionImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    PromotionRepository promotionRepository;

    public Page<Promotion> searchAll(String code, String name, Pageable pageable) {
        String updatedCode = generalFunction.createLikeQuery(code);
        String updatedName = generalFunction.createLikeQuery(name);
        return promotionRepository.searchAll(updatedCode, updatedName, pageable);
    }

    public Map<String, Object> save(PromotionEntityDTO airplane) {
        Map<String, Object> map = new HashMap<>();

        try {
            ModelMapper modelMapper = new ModelMapper();
            Promotion convertToairplane = modelMapper.map(airplane, Promotion.class);
            Promotion result = promotionRepository.save(convertToairplane);

            map = response.sukses(result);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> findById(UUID id) {
        Map<String, Object> map;

        Optional<Promotion> checkData = promotionRepository.findById(id);
        if (checkData.isEmpty()) {
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        } else {
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map<String, Object> update(UUID id, PromotionEntityDTO promotion) {
        Map<String, Object> map;
        try {
            Optional<Promotion> checkData = promotionRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
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

            map = response.sukses(savedPromotion);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> delete(UUID id) {
        Map<String, Object> map;
        try {
            Optional<Promotion> checkData = promotionRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            Promotion deletedPromotion = checkData.get();
            deletedPromotion.setDeletedDate(new Date());
            map = response.sukses(promotionRepository.save(deletedPromotion));
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }
}
