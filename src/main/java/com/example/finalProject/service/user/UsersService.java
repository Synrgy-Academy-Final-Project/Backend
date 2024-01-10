package com.example.finalProject.service.user;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.request.user.UserUpdateRequest;
import com.example.finalProject.model.user.UserDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@Service
public interface UsersService {

    @Transactional
    ResponseDTO deleteUser(Principal principal);

    @Transactional
    ResponseDTO createUser(Principal principal, UserUpdateRequest request);

    @Transactional
    ResponseDTO updateUser(UUID userDetailsId, UserUpdateRequest request);

    ResponseDTO findById(UUID id);

    ResponseDTO searchAll(String query, Pageable pageable);
}
