package com.hospital.management.repository;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByAppointment(Appointment appointment);
    Optional<Prescription> findByAppointmentId(Long appointmentId);
}
