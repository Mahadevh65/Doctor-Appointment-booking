package com.hospital.management.service;

import com.hospital.management.dto.request.PatientProfileRequest;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.dto.response.PatientResponse;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.User;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public Patient getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
    }

    // public List<Patient> getAllPatients() {
    // return patientRepository.findAll();
    // }

    public List<PatientResponse> getAllPatients() {

        List<Object[]> rows = patientRepository.getPatientDetails();

        return rows.stream().map(row -> {

            PatientResponse response = new PatientResponse();

            response.setId((Long) row[0]);
            response.setUserId((Long) row[1]);

            response.setFirstName((String) row[2]);
            response.setLastName((String) row[3]);
            response.setEmail((String) row[4]);
            response.setPhone((String) row[5]);

            response.setDateOfBirth((LocalDate) row[6]);
            response.setGender((String) row[7]);
            response.setBloodGroup((String) row[8]);

            response.setAddress((String) row[9]);
            response.setCity((String) row[10]);
            response.setState((String) row[11]);
            response.setPincode((String) row[12]);

            response.setEmergencyContactName((String) row[13]);
            response.setEmergencyContactPhone((String) row[14]);

            response.setAllergies((String) row[15]);
            response.setMedicalHistory((String) row[16]);

            return response;

        }).toList();
    }

    @Transactional
    public Patient updateProfile(Long userId, PatientProfileRequest req) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    return Patient.builder().user(user).build();
                });

        if (req.getDateOfBirth() != null)
            patient.setDateOfBirth(req.getDateOfBirth());
        if (req.getGender() != null)
            patient.setGender(req.getGender());
        if (req.getBloodGroup() != null)
            patient.setBloodGroup(req.getBloodGroup());
        if (req.getAddress() != null)
            patient.setAddress(req.getAddress());
        if (req.getCity() != null)
            patient.setCity(req.getCity());
        if (req.getState() != null)
            patient.setState(req.getState());
        if (req.getPincode() != null)
            patient.setPincode(req.getPincode());
        if (req.getEmergencyContactName() != null)
            patient.setEmergencyContactName(req.getEmergencyContactName());
        if (req.getEmergencyContactPhone() != null)
            patient.setEmergencyContactPhone(req.getEmergencyContactPhone());
        if (req.getAllergies() != null)
            patient.setAllergies(req.getAllergies());
        if (req.getMedicalHistory() != null)
            patient.setMedicalHistory(req.getMedicalHistory());

        User user = patient.getUser();
        if (req.getFirstName() != null)
            user.setFirstName(req.getFirstName());
        if (req.getLastName() != null)
            user.setLastName(req.getLastName());
        if (req.getPhone() != null)
            user.setPhone(req.getPhone());
        userRepository.save(user);

        return patientRepository.save(patient);
    }
}
