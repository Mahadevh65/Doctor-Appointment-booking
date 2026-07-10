package com.hospital.management.dto.response;

import com.hospital.management.entity.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {

    private Long id;

    private Long patientId;

    private Long doctorId;

    private String transactionId;

    private BigDecimal amount;

    private String paymentMethod;

    private String paymentStatus;

    private LocalDateTime paymentDate;

    public static PaymentResponse from(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());

        response.setPatientId(payment.getPatient().getId());

        response.setDoctorId(payment.getDoctor().getId());

        response.setTransactionId(payment.getTransactionId());

        response.setAmount(payment.getAmount());

        response.setPaymentMethod(payment.getPaymentMethod().name());

        response.setPaymentStatus(payment.getPaymentStatus().name());

        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }

}