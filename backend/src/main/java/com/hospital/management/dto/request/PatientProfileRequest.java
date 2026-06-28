package com.hospital.management.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientProfileRequest {
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String allergies;
    private String medicalHistory;
    // User fields
    private String firstName;
    private String lastName;
    private String phone;
}
