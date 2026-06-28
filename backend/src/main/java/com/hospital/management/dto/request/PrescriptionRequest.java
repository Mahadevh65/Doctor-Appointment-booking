package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionRequest {
    @NotNull private Long appointmentId;
    private String medications;
    private String dosageInstructions;
    private String additionalNotes;
    private String followUpDate;
    private String doctorNotes;
}
