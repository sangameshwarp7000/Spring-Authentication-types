package com.example.demo.service;

import com.example.demo.entity.SignupRequest;
import com.example.demo.entity.SignupResponse;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserDetailsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SignupService {

    private final UserDetailsRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupService(
            UserDetailsRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignupResponse signup(SignupRequest request) {
        if (request.username() == null || request.username().isBlank()
                || request.password() == null || request.password().isBlank()
                || request.role() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Username, password, and role are required");
        }

        if (userRepository.findByName(request.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username already exists");
        }

        Users user = new Users();
        user.setUserName(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        Users savedUser = userRepository.save(user);
        return new SignupResponse(savedUser.getId(), savedUser.getUsername(),
                savedUser.getRole());
    }
}