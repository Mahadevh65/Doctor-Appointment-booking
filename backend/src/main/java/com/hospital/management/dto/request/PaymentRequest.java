package com.hospital.management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull(message = "Doctor Id is required")
    private Long doctorId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Payment Method is required")
    private String paymentMethod;

}