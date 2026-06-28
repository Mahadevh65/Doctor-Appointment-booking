package com.hospital.management.service;

import com.hospital.management.dto.response.DashboardStatsResponse;
import com.hospital.management.entity.enums.AppointmentStatus;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository        userRepository;
    private final DoctorRepository      doctorRepository;
    private final PatientRepository     patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DashboardStatsResponse getDashboardStats() {
        return DashboardStatsResponse.builder()
                .totalDoctors(doctorRepository.count())
                .totalPatients(patientRepository.count())
                .totalAppointments(appointmentRepository.count())
                .pendingAppointments(appointmentRepository.countByStatus(AppointmentStatus.PENDING))
                .approvedAppointments(appointmentRepository.countByStatus(AppointmentStatus.APPROVED))
                .completedAppointments(appointmentRepository.countByStatus(AppointmentStatus.COMPLETED))
                .todaysAppointments(appointmentRepository.countByAppointmentDate(LocalDate.now()))
                .build();
    }
}
