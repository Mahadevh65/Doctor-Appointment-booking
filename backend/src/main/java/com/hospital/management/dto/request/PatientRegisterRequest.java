package com.hospital.management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientRegisterRequest extends RegisterRequest {

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    @NotBlank
    private String bloodGroup;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String state;

    @NotBlank
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must contain exactly 6 digits")
    private String pincode;

    @NotBlank
    @Size(max = 100)
    private String emergencyContactName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$", message = "Emergency contact must contain exactly 10 digits")
    private String emergencyContactPhone;

    @Size(max = 500)
    private String allergies;

    @Size(max = 1000)
    private String medicalHistory;
}