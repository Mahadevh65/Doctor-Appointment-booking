package com.hospital.management.repository;

import com.hospital.management.entity.Patient;
import com.hospital.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);

    Optional<Patient> findByUserId(Long userId);

    @Query("""
            SELECT
                p.id,
                u.id,
                u.firstName,
                u.lastName,
                u.email,
                u.phone,
                p.dateOfBirth,
                p.gender,
                p.bloodGroup,
                p.address,
                p.city,
                p.state,
                p.pincode,
                p.emergencyContactName,
                p.emergencyContactPhone,
                p.allergies,
                p.medicalHistory
            FROM Patient p
            JOIN p.user u
            """)
    List<Object[]> getPatientDetails();
}
