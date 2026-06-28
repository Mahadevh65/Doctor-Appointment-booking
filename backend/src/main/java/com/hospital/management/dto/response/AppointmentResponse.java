package com.hospital.management.dto.response;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AppointmentResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;
    private String reasonForVisit;
    private String symptoms;
    private String doctorNotes;
    private String rejectionReason;
    private LocalDateTime createdAt;

    public static AppointmentResponse from(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.setId(a.getId());
        r.setPatientId(a.getPatient().getId());
        r.setPatientName(a.getPatient().getUser().getFirstName()
                + " " + a.getPatient().getUser().getLastName());
        r.setDoctorId(a.getDoctor().getId());
        r.setDoctorName(a.getDoctor().getUser().getFirstName()
                + " " + a.getDoctor().getUser().getLastName());
        r.setDoctorSpecialization(a.getDoctor().getSpecialization());
        r.setAppointmentDate(a.getAppointmentDate());
        r.setAppointmentTime(a.getAppointmentTime());
        r.setStatus(a.getStatus());
        r.setReasonForVisit(a.getReasonForVisit());
        r.setSymptoms(a.getSymptoms());
        r.setDoctorNotes(a.getDoctorNotes());
        r.setRejectionReason(a.getRejectionReason());
        r.setCreatedAt(a.getCreatedAt());
        return r;
    }
}
