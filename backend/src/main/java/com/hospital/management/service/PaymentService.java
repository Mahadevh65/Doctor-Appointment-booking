package com.hospital.management.service;

import com.hospital.management.dto.request.PaymentRequest;
import com.hospital.management.dto.response.PaymentResponse;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Payment;
import com.hospital.management.entity.enums.PaymentMethod;
import com.hospital.management.entity.enums.PaymentStatus;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // =====================================================
    // Make Payment
    // =====================================================

    @Transactional
    public PaymentResponse makePayment(Long userId, PaymentRequest request) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found"));

        Payment payment = Payment.builder()
                .patient(patient)
                .doctor(doctor)
                .amount(request.getAmount())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()))
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId(generateTransactionId())
                .build();

        return PaymentResponse.from(paymentRepository.save(payment));
    }

    // =====================================================
    // Patient Payment History
    // =====================================================

    public List<PaymentResponse> getPaymentHistory(Long userId) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient not found"));

        return paymentRepository.findByPatientOrderByPaymentDateDesc(patient)
                .stream()
                .map(PaymentResponse::from)
                .toList();
    }

    // =====================================================
    // Payment Details
    // =====================================================

    public PaymentResponse getPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        return PaymentResponse.from(payment);
    }

    // =====================================================
    // Admin - All Payments
    // =====================================================

    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAllByOrderByPaymentDateDesc()
                .stream()
                .map(PaymentResponse::from)
                .toList();
    }

    // =====================================================
    // Generate Transaction Id
    // =====================================================

    private String generateTransactionId() {

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        long random = (long) (Math.random() * 9000) + 1000;

        return "HMS" + date + random;
    }

}