package com.hospital.management.controller;

import com.hospital.management.dto.request.DoctorProfileRequest;
import com.hospital.management.dto.request.RegisterRequest;
import com.hospital.management.dto.response.AppointmentResponse;
import com.hospital.management.dto.response.DashboardStatsResponse;
import com.hospital.management.dto.response.DoctorResponse;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.entity.Patient;
import com.hospital.management.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService       adminService;
    private final DoctorService      doctorService;
    private final PatientService     patientService;
    private final AppointmentService appointmentService;
    private final AuthService        authService;

    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // Doctors management
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PostMapping("/doctors")
    public ResponseEntity<MessageResponse> addDoctor(
            @Valid @RequestBody RegisterRequest req) {
        req.setRoles(java.util.Set.of("doctor"));
        return ResponseEntity.ok(authService.registerUser(req));
    }

    @PutMapping("/doctors/{id}/deactivate")
    public ResponseEntity<MessageResponse> deactivateDoctor(@PathVariable Long id) {
        doctorService.deactivateDoctor(id);
        return ResponseEntity.ok(new MessageResponse("Doctor deactivated successfully"));
    }

    @PutMapping("/doctors/{id}/activate")
    public ResponseEntity<MessageResponse> activateDoctor(@PathVariable Long id) {
        doctorService.activateDoctor(id);
        return ResponseEntity.ok(new MessageResponse("Doctor activated successfully"));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<MessageResponse> deleteDoctor(@PathVariable Long id) {
        doctorService.deactivateDoctor(id);
        return ResponseEntity.ok(new MessageResponse("Doctor removed successfully"));
    }

    // Patients management
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // Appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }
}
