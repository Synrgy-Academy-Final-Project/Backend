package com.example.finalProject.service.user;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.request.user.UserUpdateRequest;
import com.example.finalProject.dto.response.user.UserUpdateResponse;
import com.example.finalProject.model.user.User;
import com.example.finalProject.model.user.UserDetails;
import com.example.finalProject.repository.user.UserDetailsRepository;
import com.example.finalProject.repository.user.UserRepository;
import com.example.finalProject.utils.Config;
import com.example.finalProject.utils.GeneralFunction;
import com.example.finalProject.utils.Response;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService{
    private final static Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
    private final UserRepository usersRepository;
    private final Response response;
    private final AuthenticationServiceImpl authenticationService;
    private final UserDetailsRepository userDetailsRepository;
    private final GeneralFunction generalFunction;

    @Transactional
    @Override
    public ResponseDTO deleteUser(Principal principal) {
        try {
            User idUser = authenticationService.getIdUser(principal.getName());
            if(idUser.isUserActive() && idUser.getDeletedDate() == null){
                idUser.setUserActive(false);
                idUser.setDeletedDate(Timestamp.valueOf(LocalDateTime.now()));
                usersRepository.save(idUser);
                logger.info("Success delete user");
                return response.suksesDTO("Account has been deleted");
            }else {
                return response.errorDTO(404, "Account is not active");
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            return response.errorDTO(404, e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDTO createUser(Principal principal, UserUpdateRequest request) {
        try {
            User idUser = authenticationService.getIdUser(principal.getName());
            if (idUser.getUsersDetails() == null){
                UUID randId = UUID.randomUUID();
                UserDetails userDetails = new UserDetails();
                userDetails.setId(randId);
//            userDetails.setUser(idUser);
                userDetails.setAddress(request.getAddress());
                userDetails.setGender(request.getGender());
                userDetails.setPhoneNumber(request.getPhoneNumber());
                userDetails.setVisa(request.getVisa());
                userDetails.setPassport(request.getPassport());
                userDetails.setResidentPermit(request.getResidentPermit());
                userDetails.setNik(request.getNik());
                userDetails.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                userDetails.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
                userDetailsRepository.save(userDetails);

                idUser.setUsersDetails(userDetails);
                usersRepository.save(idUser);

                UserUpdateResponse userUpdateResponse = new UserUpdateResponse();
                userUpdateResponse.setAddress(userDetails.getAddress());
                userUpdateResponse.setGender(userDetails.getGender());
                userUpdateResponse.setPhoneNumber(userDetails.getPhoneNumber());
                userUpdateResponse.setVisa(userDetails.getVisa());
                userUpdateResponse.setPassport(userDetails.getPassport());
                userUpdateResponse.setResidentPermit(userDetails.getResidentPermit());
                userUpdateResponse.setNik(userDetails.getNik());

                return response.suksesDTO(userUpdateResponse);
            }else {
                return response.errorDTO(400, "User has profile");
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            return response.errorDTO(404, e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseDTO updateUser(UUID userDetailsId, UserUpdateRequest request) {
        try {
            Optional<UserDetails> checkUserDetails = userDetailsRepository.findById(userDetailsId);

            if (checkUserDetails.isEmpty()){
                return response.errorDTO(404, Config.DATA_NOT_FOUND);
            }

            UserDetails userDetails = checkUserDetails.get();
            userDetails.setAddress(request.getAddress());
            userDetails.setGender(request.getGender());
            userDetails.setPhoneNumber(request.getPhoneNumber());
            userDetails.setVisa(request.getVisa());
            userDetails.setPassport(request.getPassport());
            userDetails.setResidentPermit(request.getResidentPermit());
            userDetails.setNik(request.getNik());
            userDetails.setCreatedDate(userDetails.getCreatedDate());
            userDetails.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

            UserUpdateResponse userUpdateResponse = new UserUpdateResponse();
            userUpdateResponse.setAddress(userDetails.getAddress());
            userUpdateResponse.setGender(userDetails.getGender());
            userUpdateResponse.setPhoneNumber(userDetails.getPhoneNumber());
            userUpdateResponse.setVisa(userDetails.getVisa());
            userUpdateResponse.setPassport(userDetails.getPassport());
            userUpdateResponse.setResidentPermit(userDetails.getResidentPermit());
            userUpdateResponse.setNik(userDetails.getNik());

            return response.suksesDTO(userUpdateResponse);
        }catch (Exception e){
            logger.error(e.getMessage());
            return response.errorDTO(404, e.getMessage());
        }
    }

    @Override
    public ResponseDTO findById(UUID id) {
        Optional<UserDetails> checkData= userDetailsRepository.findById(id);
        if (checkData.isEmpty()){
            return response.errorDTO(404, Config.DATA_NOT_FOUND);
        }else{
            UserUpdateResponse userUpdateResponse = new UserUpdateResponse();
            userUpdateResponse.setAddress(checkData.get().getAddress());
            userUpdateResponse.setGender(checkData.get().getGender());
            userUpdateResponse.setPhoneNumber(checkData.get().getPhoneNumber());
            userUpdateResponse.setVisa(checkData.get().getVisa());
            userUpdateResponse.setPassport(checkData.get().getPassport());
            userUpdateResponse.setResidentPermit(checkData.get().getResidentPermit());
            userUpdateResponse.setNik(checkData.get().getNik());
            return response.suksesDTO(userUpdateResponse);
        }
    }

    @Override
    public ResponseDTO searchAll(String query, Pageable pageable) {
        String updatedQuery = generalFunction.createLikeQuery(query);
        return response.suksesDTO(userDetailsRepository.searchAll(updatedQuery, pageable));
    }
}
