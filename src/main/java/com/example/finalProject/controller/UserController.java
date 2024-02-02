package com.example.finalProject.controller;

import com.example.finalProject.dto.ResponseDTO;
import com.example.finalProject.dto.request.user.UserUpdateRequest;
import com.example.finalProject.service.user.UsersServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UsersServiceImpl usersService;

    @GetMapping({"", "/"})
    public ResponseEntity<ResponseDTO> searchUsers(@RequestParam(defaultValue = "0") int pageNumber,
                                                   @RequestParam(defaultValue = "100") int pageSize,
                                                   @RequestParam(defaultValue = "") String sortBy,
                                                   @ModelAttribute("name") String name){
        Pageable pageable;
        if (sortBy.isEmpty()){
            System.out.println("true");
            pageable = PageRequest.of(pageNumber, pageSize);
        }else{
            System.out.println("false");
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        }
        ResponseDTO result = usersService.searchAll(name, pageable);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO> createUser(Principal principal, @RequestBody UserUpdateRequest request){
        ResponseDTO result = usersService.createUser(principal, request);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findUser(@PathVariable UUID id){
        ResponseDTO result = usersService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request){
        ResponseDTO result = usersService.updateUser(id, request);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDTO> deleteUser(Principal principal){
        ResponseDTO result = usersService.deleteUser(principal);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatus()));
    }
}
