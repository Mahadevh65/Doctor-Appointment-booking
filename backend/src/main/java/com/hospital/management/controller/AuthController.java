package com.hospital.management.controller;

import com.hospital.management.dto.request.LoginRequest;
import com.hospital.management.dto.request.RegisterRequest;
import com.hospital.management.dto.response.JwtResponse;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(
            @Valid @RequestBody RegisterRequest signUpRequest) {
        return ResponseEntity.ok(authService.registerUser(signUpRequest));
    }
}
