package com.hospital.management.controller;

import com.hospital.management.dto.request.AppointmentRequest;
import com.hospital.management.dto.request.PatientProfileRequest;
import com.hospital.management.dto.response.AppointmentResponse;
import com.hospital.management.entity.Patient;
import com.hospital.management.security.services.UserDetailsImpl;
import com.hospital.management.service.AppointmentService;
import com.hospital.management.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final PatientService     patientService;
    private final AppointmentService appointmentService;

    @GetMapping("/profile")
    public ResponseEntity<Patient> getProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                patientService.getPatientByUserId(userDetails.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<Patient> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PatientProfileRequest req) {
        return ResponseEntity.ok(
                patientService.updateProfile(userDetails.getId(), req));
    }

    @PostMapping("/appointments/book")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AppointmentRequest req) {
        return ResponseEntity.ok(
                appointmentService.bookAppointment(userDetails.getId(), req));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                appointmentService.getPatientAppointments(userDetails.getId()));
    }

    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id, userDetails.getId()));
    }
}
