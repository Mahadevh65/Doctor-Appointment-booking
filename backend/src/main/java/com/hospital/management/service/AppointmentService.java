package com.hospital.management.service;

import com.hospital.management.dto.request.AppointmentRequest;
import com.hospital.management.dto.request.PrescriptionRequest;
import com.hospital.management.dto.response.AppointmentResponse;
import com.hospital.management.entity.*;
import com.hospital.management.entity.enums.AppointmentStatus;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository     patientRepository;
    private final DoctorRepository      doctorRepository;
    private final PrescriptionRepository prescriptionRepository;

    // ─── Patient actions ────────────────────────────────────

    @Transactional
    public AppointmentResponse bookAppointment(Long userId, AppointmentRequest req) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient profile not found. Please complete your profile first."));
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        boolean slotTaken = appointmentRepository
                .existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatusNot(
                        doctor, req.getAppointmentDate(),
                        req.getAppointmentTime(), AppointmentStatus.CANCELLED);
        if (slotTaken)
            throw new IllegalArgumentException(
                    "This slot is already booked. Please choose a different time.");

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .reasonForVisit(req.getReasonForVisit())
                .symptoms(req.getSymptoms())
                .status(AppointmentStatus.PENDING)
                .build();

        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId, Long userId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (!appointment.getPatient().getUser().getId().equals(userId))
            throw new IllegalArgumentException("You can only cancel your own appointments.");
        if (appointment.getStatus() == AppointmentStatus.COMPLETED)
            throw new IllegalArgumentException("Cannot cancel a completed appointment.");

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getPatientAppointments(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return appointmentRepository.findByPatientOrderByCreatedAtDesc(patient)
                .stream().map(AppointmentResponse::from).collect(Collectors.toList());
    }

    // ─── Doctor actions ──────────────────────────────────────

    public List<AppointmentResponse> getDoctorAppointments(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return appointmentRepository
                .findByDoctorOrderByAppointmentDateAscAppointmentTimeAsc(doctor)
                .stream().map(AppointmentResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponse approveAppointment(Long appointmentId, Long userId) {
        Appointment appointment = findAndVerifyDoctorOwnership(appointmentId, userId);
        if (appointment.getStatus() != AppointmentStatus.PENDING)
            throw new IllegalArgumentException("Only PENDING appointments can be approved.");
        appointment.setStatus(AppointmentStatus.APPROVED);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse rejectAppointment(Long appointmentId,
                                                  Long userId, String reason) {
        Appointment appointment = findAndVerifyDoctorOwnership(appointmentId, userId);
        if (appointment.getStatus() != AppointmentStatus.PENDING)
            throw new IllegalArgumentException("Only PENDING appointments can be rejected.");
        appointment.setStatus(AppointmentStatus.REJECTED);
        appointment.setRejectionReason(reason);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse addPrescription(Long userId, PrescriptionRequest req) {
        Appointment appointment = findAndVerifyDoctorOwnership(req.getAppointmentId(), userId);

        appointment.setDoctorNotes(req.getDoctorNotes());
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        Prescription prescription = prescriptionRepository
                .findByAppointment(appointment).orElse(new Prescription());
        prescription.setAppointment(appointment);
        prescription.setMedications(req.getMedications());
        prescription.setDosageInstructions(req.getDosageInstructions());
        prescription.setAdditionalNotes(req.getAdditionalNotes());
        prescription.setFollowUpDate(req.getFollowUpDate());
        prescriptionRepository.save(prescription);

        return AppointmentResponse.from(appointment);
    }

    // ─── Admin actions ───────────────────────────────────────

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAllOrderByCreatedAtDesc()
                .stream().map(AppointmentResponse::from).collect(Collectors.toList());
    }

    // ─── Helpers ─────────────────────────────────────────────

    private Appointment findAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));
    }

    private Appointment findAndVerifyDoctorOwnership(Long appointmentId, Long userId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (!appointment.getDoctor().getUser().getId().equals(userId))
            throw new IllegalArgumentException(
                    "You are not authorized to modify this appointment.");
        return appointment;
    }
}
