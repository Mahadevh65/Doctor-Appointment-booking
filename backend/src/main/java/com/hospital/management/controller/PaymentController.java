package com.hospital.management.controller;

import com.hospital.management.dto.request.PaymentRequest;
import com.hospital.management.dto.response.PaymentResponse;
import com.hospital.management.security.services.UserDetailsImpl;
import com.hospital.management.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    private final PaymentService paymentService;

    // =====================================================
    // Patient Make Payment
    // =====================================================

    @PostMapping("/pay")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PaymentResponse> makePayment(

            @AuthenticationPrincipal UserDetailsImpl userDetails,

            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.makePayment(userDetails.getId(), request));
    }

    // =====================================================
    // Patient Payment History
    // =====================================================

    @GetMapping("/history")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<PaymentResponse>> paymentHistory(

            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(
                paymentService.getPaymentHistory(userDetails.getId()));
    }

    // =====================================================
    // Payment Details
    // =====================================================

    @GetMapping("/{paymentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> getPayment(

            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getPayment(paymentId));
    }

    // =====================================================
    // Admin - View All Payments
    // =====================================================

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments());
    }

}