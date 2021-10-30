package com.example.controller;

import com.example.dto.UserDTO;
import com.example.service.RegisterUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final RegisterUser registerUser;

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws Exception {
        registerUser.register(userDTO.getUserName(), userDTO.getAffiliation(), userDTO.getOrg());
        return ResponseEntity.accepted().body(userDTO);
    }
}
