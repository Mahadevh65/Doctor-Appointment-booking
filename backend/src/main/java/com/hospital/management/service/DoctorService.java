package com.hospital.management.service;

import com.hospital.management.dto.request.DoctorProfileRequest;
import com.hospital.management.dto.response.DoctorResponse;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.User;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository   userRepository;

    public List<DoctorResponse> getAllActiveDoctors() {
        return doctorRepository.findByActiveTrue()
                .stream().map(DoctorResponse::from).collect(Collectors.toList());
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream().map(DoctorResponse::from).collect(Collectors.toList());
    }

    public DoctorResponse getDoctorById(Long id) {
        return DoctorResponse.from(doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found")));
    }

    public DoctorResponse getDoctorProfile(Long userId) {
        return DoctorResponse.from(doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found")));
    }

    @Transactional
    public DoctorResponse updateDoctorProfile(Long userId, DoctorProfileRequest req) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    return Doctor.builder().user(user).consultationFee(0.0).build();
                });

        if (req.getSpecialization()    != null) doctor.setSpecialization(req.getSpecialization());
        if (req.getQualification()     != null) doctor.setQualification(req.getQualification());
        if (req.getLicenseNumber()     != null) doctor.setLicenseNumber(req.getLicenseNumber());
        if (req.getExperienceYears()   != null) doctor.setExperienceYears(req.getExperienceYears());
        if (req.getDepartment()        != null) doctor.setDepartment(req.getDepartment());
        if (req.getBio()               != null) doctor.setBio(req.getBio());
        if (req.getConsultationFee()   != null) doctor.setConsultationFee(req.getConsultationFee());
        if (req.getAvailableDays()     != null) doctor.setAvailableDays(req.getAvailableDays());
        if (req.getAvailableTimeStart()!= null) doctor.setAvailableTimeStart(req.getAvailableTimeStart());
        if (req.getAvailableTimeEnd()  != null) doctor.setAvailableTimeEnd(req.getAvailableTimeEnd());

        // Update user fields
        User user = doctor.getUser();
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName()  != null) user.setLastName(req.getLastName());
        if (req.getPhone()     != null) user.setPhone(req.getPhone());
        userRepository.save(user);

        return DoctorResponse.from(doctorRepository.save(doctor));
    }

    @Transactional
    public void deactivateDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }

    @Transactional
    public void activateDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setActive(true);
        doctorRepository.save(doctor);
    }

    public List<DoctorResponse> searchDoctors(String keyword) {
        return doctorRepository.searchDoctors(keyword)
                .stream().map(DoctorResponse::from).collect(Collectors.toList());
    }
}
