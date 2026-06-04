package com.rishi.jobtracker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.jobtracker.dto.ApiResponse;
import com.rishi.jobtracker.dto.LoginRequestDTO;
import com.rishi.jobtracker.dto.LoginResponseDTO;
import com.rishi.jobtracker.dto.RegisterRequestDTO;
import com.rishi.jobtracker.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse register(
            @Valid @RequestBody
            RegisterRequestDTO request) {

        return authService.register(request);
    }

    @PostMapping("/login")
public LoginResponseDTO login(
        @Valid @RequestBody
        LoginRequestDTO request) {

    return authService.login(request);
}
}