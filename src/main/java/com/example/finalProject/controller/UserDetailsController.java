package com.example.finalProject.controller;

import com.example.finalProject.dto.AirportEntityDTO;
import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.request.user.UserUpdateRequest;
import com.example.finalProject.model.user.User;
import com.example.finalProject.model.user.UserDetails;
import com.example.finalProject.service.AirportImpl;
import com.example.finalProject.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user-detail")
@Slf4j
public class UserDetailsController {
    private final UserDetailsImpl userDetailsImpl;

    @GetMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> searchUserDetails(@RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "100") int pageSize,
                                                         @RequestParam(defaultValue = "") String sortBy
    ) {
        Pageable pageable;
        if (sortBy.isEmpty()) {
            pageable = PageRequest.of(pageNumber, pageSize);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = userDetailsImpl.searchAll(pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> addUserDetails(@RequestBody @Validated UserUpdateRequest userDetails) {
        ResponseDTO result = userDetailsImpl.save(userDetails);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping({ "", "/" })
    public ResponseEntity<ResponseDTO> updateUserDetails(@RequestBody UserUpdateRequest userDetails,
                                                         Principal principal) {
        User data = (User) userDetailsImpl.findByEmail(principal.getName()).getData();
        UserDetails userDetailData = data.getUsersDetails();
        ResponseDTO result = userDetailsImpl.update(userDetailData.getId(), userDetails);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping("/logged-in-user")
    public ResponseEntity<ResponseDTO> findUser(Principal principal){
        ResponseDTO result = userDetailsImpl.findByEmail(principal.getName());
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> findUserDetails(@PathVariable UUID id) {
        ResponseDTO result = userDetailsImpl.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping({ "{id}", "{id}/" })
    public ResponseEntity<ResponseDTO> deleteUserDetails(@PathVariable UUID id) {
        ResponseDTO result = userDetailsImpl.delete(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
