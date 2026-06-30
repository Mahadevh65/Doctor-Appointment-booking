package com.hospital.management.controller;

import com.hospital.management.dto.request.DoctorRegisterRequest;
import com.hospital.management.dto.request.LoginRequest;
import com.hospital.management.dto.request.PatientRegisterRequest;
import com.hospital.management.dto.request.RegisterRequest;
import com.hospital.management.dto.response.JwtResponse;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hospital.management.dto.request.DoctorRegisterRequest;
import com.hospital.management.dto.request.PatientRegisterRequest;

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

    @PostMapping("/register/doctor")
    public ResponseEntity<MessageResponse> registerDoctor(
            @Valid @RequestBody DoctorRegisterRequest request) {

        return ResponseEntity.ok(authService.registerDoctor(request));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<MessageResponse> registerPatient(
            @Valid @RequestBody PatientRegisterRequest request) {

        return ResponseEntity.ok(authService.registerPatient(request));
    }
}
