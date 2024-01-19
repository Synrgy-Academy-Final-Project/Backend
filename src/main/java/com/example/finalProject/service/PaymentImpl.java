package com.example.finalProject.service;

import com.example.finalProject.dto.MidtransResponseDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.entity.Payment;
import com.example.finalProject.repository.PaymentRepository;
import com.example.finalProject.repository.TransactionRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import jakarta.transaction.Transactional;
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
    @Autowired
    TransactionRepository transactionRepository;

    public ResponseDTO searchAll(String accountNumber, String bankName, Pageable pageable) {

        return response.suksesDTO(paymentRepository.searchAll(pageable));
    }

//    public ResponseDTO save(PaymentEntityDTO payment) {
//        try {
//            ModelMapper modelMapper = new ModelMapper();
//            Payment convertToairplane = modelMapper.map(payment, Payment.class);
//            Payment result = paymentRepository.save(convertToairplane);
//
//            return response.suksesDTO(result);
//        } catch (Exception e) {
//            return response.errorDTO(500, e.getMessage());
//        }
//    }
    @Transactional
    public ResponseDTO saveMidtrans(MidtransResponseDTO midtransResponse) {
        try {
            if(!generalFunction.validateMidtransResponse(midtransResponse)){
                return response.errorDTO(422, "invalid payment response");
            }

            if (transactionRepository.findById(midtransResponse.getOrder_id()).isEmpty()){
                return response.dataNotFound("Transactions");
            }

            paymentRepository.deleteByTransactionId(midtransResponse.getOrder_id());

            ModelMapper modelMapper = new ModelMapper();
            Payment convertToPayment = modelMapper.map(midtransResponse, Payment.class);

            convertToPayment.setId(midtransResponse.getTransaction_id());
            convertToPayment.setTransaction(transactionRepository.findById(midtransResponse.getOrder_id()).get());
            convertToPayment.setGrossAmount(Double.parseDouble(midtransResponse.getGross_amount()));

            Payment result = paymentRepository.save(convertToPayment);

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

//    public ResponseDTO update(UUID id, PaymentEntityDTO payment) {
//        try {
//            Optional<Payment> checkData = paymentRepository.findById(id);
//            if (checkData.isEmpty()) {
//                return response.dataNotFound("Payment");
//            }
//
//            Payment updatedPayment = checkData.get();
//
//            if (payment.getAccountName() != null) {
//                updatedPayment.setAccountName(payment.getAccountName());
//            }
//            if (payment.getAccountNumber() != null) {
//                updatedPayment.setAccountNumber(payment.getAccountNumber());
//            }
//            if (payment.getBankName() != null) {
//                updatedPayment.setBankName(payment.getBankName());
//            }
//
//            // Save the updated Payment
//            Payment savePayment = paymentRepository.save(updatedPayment);
//
//            return response.suksesDTO(savePayment);
//        } catch (Exception e) {
//            return response.errorDTO(500, e.getMessage());
//        }
//    }

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
