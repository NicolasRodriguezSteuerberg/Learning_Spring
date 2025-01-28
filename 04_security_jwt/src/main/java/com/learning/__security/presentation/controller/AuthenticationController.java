package com.learning.__security.presentation.controller;

import com.learning.__security.presentation.dto.request.AuthLoginRequest;
import com.learning.__security.presentation.dto.request.AuthRegisterRequest;
import com.learning.__security.presentation.dto.response.AuthResponse;
import com.learning.__security.service.implementation.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(userDetailService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRegisterRequest userRequest) {
        return new ResponseEntity<>(userDetailService.createUser(userRequest), HttpStatus.CREATED);
    }





}
