package com.hospital.management.repository;

import com.hospital.management.entity.ConsultationMessage;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationMessageRepository
        extends JpaRepository<ConsultationMessage, Long> {

    // Complete conversation between one patient and one doctor
    List<ConsultationMessage> findByPatientAndDoctorOrderBySentAtAsc(
            Patient patient,
            Doctor doctor);

    // All messages received by a doctor
    List<ConsultationMessage> findByDoctorOrderBySentAtDesc(
            Doctor doctor);

    // All messages sent by a patient
    List<ConsultationMessage> findByPatientOrderBySentAtDesc(
            Patient patient);
}