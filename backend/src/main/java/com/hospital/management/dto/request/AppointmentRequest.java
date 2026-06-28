package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {

    @NotNull private Long doctorId;
    @NotNull private LocalDate appointmentDate;
    @NotNull private LocalTime appointmentTime;

    private String reasonForVisit;
    private String symptoms;
}
