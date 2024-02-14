package com.example.finalProject.service;

import com.example.finalProject.dto.CompanyEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.request.user.UserUpdateRequest;
import com.example.finalProject.entity.Company;
import com.example.finalProject.model.user.User;
import com.example.finalProject.model.user.UserDetails;
import com.example.finalProject.repository.CompanyRepository;
import com.example.finalProject.repository.user.UserDetailsRepository;
import com.example.finalProject.repository.user.UserRepository;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailsImpl {
    @Autowired
    Response response;
    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    GeneralFunction generalFunction;

    public ResponseDTO searchAll(Pageable pageable) {
        return response.suksesDTO(userDetailsRepository.findAll(pageable));
    }

    public ResponseDTO save(UserUpdateRequest userDetails) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            UserDetails convertToUserDetails = modelMapper.map(userDetails, UserDetails.class);
            UserDetails result = userDetailsRepository.save(convertToUserDetails);
            return response.suksesDTO(result);
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO findById(UUID id) {
        Optional<UserDetails> checkData = userDetailsRepository.findById(id);
        if (checkData.isEmpty()){
            return response.dataNotFound("UserDetails");
        }else{
            return response.suksesDTO(checkData.get());
        }
    }

    public ResponseDTO findByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()){
            return response.dataNotFound("User");
        }
        return response.suksesDTO(user.get());
    }

    public ResponseDTO update(UUID id, UserUpdateRequest userDetails) {
        try{
            Optional<UserDetails> checkData = userDetailsRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("UserDetails");
            }

            UserDetails updatedUserDetails = checkData.get();

            if (userDetails.getFirstName() != null){
                updatedUserDetails.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getLastName() != null){
                updatedUserDetails.setLastName(userDetails.getLastName());
            }
            if (userDetails.getDateOfBirth() != null){
                updatedUserDetails.setDateOfBirth(userDetails.getDateOfBirth());
            }
            if (userDetails.getAddress() != null){
                updatedUserDetails.setAddress(userDetails.getAddress());
            }
            if (userDetails.getGender() != null){
                updatedUserDetails.setGender(userDetails.getGender());
            }
            if (userDetails.getPhoneNumber() != null){
                updatedUserDetails.setPhoneNumber(userDetails.getPhoneNumber());
            }
            if (userDetails.getVisa() != null){
                updatedUserDetails.setVisa(userDetails.getVisa());
            }
            if (userDetails.getPassport() != null){
                updatedUserDetails.setPassport(userDetails.getPassport());
            }
            if (userDetails.getResidentPermit() != null){
                updatedUserDetails.setResidentPermit(userDetails.getResidentPermit());
            }
            if (userDetails.getNik() != null){
                updatedUserDetails.setNIK(userDetails.getNik());
            }
            if (userDetails.getNationality() != null){
                updatedUserDetails.setNationality(userDetails.getNationality());
            }

            return response.suksesDTO(userDetailsRepository.save(updatedUserDetails));
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }

    public ResponseDTO delete(UUID id) {
        try{
            Optional<UserDetails> checkData = userDetailsRepository.findById(id);
            if(checkData.isEmpty()){
                return response.dataNotFound("UserDetails");
            }

            UserDetails deletedUserDetails = checkData.get();
            deletedUserDetails.setDeletedDate(Timestamp.from(Instant.now()));
            return response.suksesDTO(userDetailsRepository.save(deletedUserDetails));
        }catch (Exception e){
            return response.errorDTO(500, e.getMessage());
        }
    }
}
