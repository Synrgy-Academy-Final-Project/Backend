package com.example.finalProject.service;

import com.example.finalProject.dto.PaymentEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Payment;
import com.example.finalProject.repository.PaymentRepository;
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
public class PaymentImpl {
    @Autowired
    Response response;
    @Autowired
    GeneralFunction generalFunction;
    @Autowired
    PaymentRepository paymentRepository;

    public ResponseDTO searchAll(String accountNumber, String bankName, Pageable pageable) {
        String updatedAccountNumber = generalFunction.createLikeQuery(accountNumber);
        String updateBankName = generalFunction.createLikeQuery(bankName);

        return response.suksesDTO(paymentRepository.searchAll(updatedAccountNumber, updateBankName, pageable));
    }

    public ResponseDTO save(PaymentEntityDTO payment) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            Payment convertToairplane = modelMapper.map(payment, Payment.class);
            Payment result = paymentRepository.save(convertToairplane);

            return response.suksesDTO(result);
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<Payment> checkData = paymentRepository.findById(id);
        if (checkData.isEmpty()) {
            return response.dataNotFound("Payment");
        } else {
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO update(UUID id, PaymentEntityDTO payment) {
        try {
            Optional<Payment> checkData = paymentRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.dataNotFound("Payment");
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

            return response.suksesDTO(savePayment);
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try {
            Optional<Payment> checkData = paymentRepository.findById(id);
            if (checkData.isEmpty()) {
                return response.dataNotFound("Payment");
            }
            Payment deletedPayment = checkData.get();
            deletedPayment.setDeletedDate(new Date());
            return response.suksesDTO(paymentRepository.save(deletedPayment));
        } catch (Exception e) {
            return response.errorDTO(500, e.getMessage());
        }
    }

}