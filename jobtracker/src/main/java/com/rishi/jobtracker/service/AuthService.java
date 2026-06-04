package com.rishi.jobtracker.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rishi.jobtracker.dto.ApiResponse;
import com.rishi.jobtracker.dto.LoginRequestDTO;
import com.rishi.jobtracker.dto.LoginResponseDTO;
import com.rishi.jobtracker.dto.RegisterRequestDTO;
import com.rishi.jobtracker.entity.User;
import com.rishi.jobtracker.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ApiResponse register(
            RegisterRequestDTO request) {

        if (userRepository.findByEmail(
                request.getEmail()).isPresent()) {

            throw new RuntimeException(
                    "Email already registered");
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));
        userRepository.save(user);

        return new ApiResponse(
                "User registered successfully",
                "1.0",
                "success"
        );
    }

    public LoginResponseDTO login(
        LoginRequestDTO request) {

    Optional<User> optionalUser =
            userRepository.findByEmail(
                    request.getEmail());

    if (optionalUser.isEmpty()) {

        throw new RuntimeException(
                "Invalid email or password");
    }

    User user = optionalUser.get();

    boolean passwordMatches =
            passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword()
            );

    if (!passwordMatches) {

        throw new RuntimeException(
                "Invalid email or password");
    }

    String token =
            jwtService.generateToken(
                    user.getEmail());

    return new LoginResponseDTO(token);
}
}