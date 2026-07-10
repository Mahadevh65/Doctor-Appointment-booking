package com.hospital.management.repository;

import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Payment;
import com.hospital.management.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Patient payment history
    List<Payment> findByPatientOrderByPaymentDateDesc(Patient patient);

    // Admin - View all payments
    List<Payment> findAllByOrderByPaymentDateDesc();

    // Payment by Transaction Id
    Optional<Payment> findByTransactionId(String transactionId);

    // Check payment before booking appointment
    Optional<Payment> findTopByPatientAndDoctorAndPaymentStatusOrderByPaymentDateDesc(
            Patient patient,
            Doctor doctor,
            PaymentStatus paymentStatus
    );

}