package com.hospital.management.dto.response;

import com.hospital.management.entity.Doctor;
import lombok.Data;

@Data
public class DoctorResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
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
    private boolean active;

    public static DoctorResponse from(Doctor d) {
        DoctorResponse r = new DoctorResponse();
        r.setId(d.getId());
        r.setUserId(d.getUser().getId());
        r.setFirstName(d.getUser().getFirstName());
        r.setLastName(d.getUser().getLastName());
        r.setEmail(d.getUser().getEmail());
        r.setPhone(d.getUser().getPhone());
        r.setSpecialization(d.getSpecialization());
        r.setQualification(d.getQualification());
        r.setLicenseNumber(d.getLicenseNumber());
        r.setExperienceYears(d.getExperienceYears());
        r.setDepartment(d.getDepartment());
        r.setBio(d.getBio());
        r.setConsultationFee(d.getConsultationFee());
        r.setAvailableDays(d.getAvailableDays());
        r.setAvailableTimeStart(d.getAvailableTimeStart());
        r.setAvailableTimeEnd(d.getAvailableTimeEnd());
        r.setActive(d.isActive());
        return r;
    }
}
