package com.hospital.management.controller;

import com.hospital.management.dto.response.DoctorResponse;
import com.hospital.management.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicDoctorController {

    private final DoctorService doctorService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DoctorResponse>> listActiveDoctors(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(doctorService.searchDoctors(search));
        }
        return ResponseEntity.ok(doctorService.getAllActiveDoctors());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DoctorResponse> getDoctorDetails(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
}
