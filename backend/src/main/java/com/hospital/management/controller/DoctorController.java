package com.hospital.management.controller;

import com.hospital.management.dto.request.DoctorProfileRequest;
import com.hospital.management.dto.request.PrescriptionRequest;
import com.hospital.management.dto.response.AppointmentResponse;
import com.hospital.management.dto.response.DoctorResponse;
import com.hospital.management.security.services.UserDetailsImpl;
import com.hospital.management.service.AppointmentService;
import com.hospital.management.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    private final DoctorService      doctorService;
    private final AppointmentService appointmentService;

    @GetMapping("/profile")
    public ResponseEntity<DoctorResponse> getProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(doctorService.getDoctorProfile(userDetails.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<DoctorResponse> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody DoctorProfileRequest req) {
        return ResponseEntity.ok(
                doctorService.updateDoctorProfile(userDetails.getId(), req));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                appointmentService.getDoctorAppointments(userDetails.getId()));
    }

    @PutMapping("/appointments/{id}/approve")
    public ResponseEntity<AppointmentResponse> approveAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(
                appointmentService.approveAppointment(id, userDetails.getId()));
    }

    @PutMapping("/appointments/{id}/reject")
    public ResponseEntity<AppointmentResponse> rejectAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                appointmentService.rejectAppointment(id, userDetails.getId(),
                        body.getOrDefault("reason", "")));
    }

    @PostMapping("/prescriptions")
    public ResponseEntity<AppointmentResponse> addPrescription(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PrescriptionRequest req) {
        return ResponseEntity.ok(
                appointmentService.addPrescription(userDetails.getId(), req));
    }
}
