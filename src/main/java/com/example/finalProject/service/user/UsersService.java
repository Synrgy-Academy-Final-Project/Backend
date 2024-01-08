package com.example.finalProject.service.user;

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
    Map deleteUser(Principal principal);

    @Transactional
    Map createUser(Principal principal, UserUpdateRequest request);

    @Transactional
    Map updateUser(UUID userDetailsId, UserUpdateRequest request);

    Map findById(UUID id);

    Page<UserDetails> searchAll(String query, Pageable pageable);
}
