package com.hospital.management.dto.request;

import lombok.Data;

@Data
public class DoctorProfileRequest {
    private String specialization;
    private String qualification;
    private String licenseNumber;
    private Integer experienceYears;
    private String department;
    private String bio;
    private Double consultationFee;
    private String availableDays;
    private String availableTimeStart;
    private String availableTimeEnd;
    // User fields
    private String firstName;
    private String lastName;
    private String phone;
}
