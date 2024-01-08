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

import com.example.finalProject.dto.PaymentEntityDTO;
import com.example.finalProject.entity.Payment;
import com.example.finalProject.repository.PaymentRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;

@Service
public class PaymentImpl {
    @Autowired
    Response response;
    @Autowired
    Config config;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    PaymentRepository paymentRepository;

    public Page<Payment> searchAll(String accountNumber, String bankName, Pageable pageable) {
        String updatedAccountNumber = generalFunction.createLikeQuery(accountNumber);
        String updateBankName = generalFunction.createLikeQuery(bankName);
        return paymentRepository.searchAll(updatedAccountNumber, updateBankName, pageable);
    }

    public Map<String, Object> save(PaymentEntityDTO payment) {
        Map<String, Object> map = new HashMap<>();

        try {
            ModelMapper modelMapper = new ModelMapper();
            Payment convertToairplane = modelMapper.map(payment, Payment.class);
            Payment result = paymentRepository.save(convertToairplane);

            map = response.sukses(result);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> findById(UUID id) {
        Map<String, Object> map;

        Optional<Payment> checkData = paymentRepository.findById(id);
        if (checkData.isEmpty()) {
            map = response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
        } else {
            map = response.sukses(checkData.get());
        }
        return map;
    }

    public Map<String, Object> update(UUID id, PaymentEntityDTO payment) {
        Map<String, Object> map;
        try {
            Optional<Payment> checkData = paymentRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }

            Payment updatedPayment = checkData.get();

            if (payment.getAccountName() != null) {
                updatedPayment.setAccountName(payment.getAccountName());
            }
            if (payment.getAccountNumber() != null) {
                updatedPayment.setAccountNumber(payment.getAccountNumber());
            }
            if (payment.getBankName() != null) {
                updatedPayment.setBankName(payment.getBankName());
            }

            // Save the updated Payment
            Payment savePayment = paymentRepository.save(updatedPayment);

            map = response.sukses(savePayment);
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

    public Map<String, Object> delete(UUID id) {
        Map<String, Object> map;
        try {
            Optional<Payment> checkData = paymentRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.error(Config.DATA_NOT_FOUND, Config.EROR_CODE_404);
            }
            Payment deletedPayment = checkData.get();
            deletedPayment.setDeletedDate(new Date());
            map = response.sukses(paymentRepository.save(deletedPayment));
        } catch (Exception e) {
            map = response.error(e.getMessage(), Config.EROR_CODE_404);
        }
        return map;
    }

}
