package com.hospital.management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorRegisterRequest extends RegisterRequest {

    @NotBlank
    @Size(max = 100)
    private String specialization;

    @NotBlank
    @Size(max = 100)
    private String qualification;

    @NotBlank
    @Size(max = 100)
    private String licenseNumber;

    @NotBlank
    @Size(max = 100)
    private String department;

    @NotNull
    @Min(0)
    private Integer experienceYears;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double consultationFee;

    @NotBlank
    private String availableDays;

    @NotNull
    private String availableTimeStart;

    @NotNull
    private String availableTimeEnd;

    @Size(max = 500)
    private String bio;
}