package com.hospital.management.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponse {

    private Long id;
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

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
}