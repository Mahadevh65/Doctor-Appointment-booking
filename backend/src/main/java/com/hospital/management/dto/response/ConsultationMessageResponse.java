package com.hospital.management.dto.response;

import com.hospital.management.entity.ConsultationMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultationMessageResponse {

    private Long id;

    private Long patientId;

    private Long doctorId;

    private String sender;

    private String message;

    private LocalDateTime sentAt;

    private Boolean isRead;

    public static ConsultationMessageResponse from(ConsultationMessage consultationMessage) {

        ConsultationMessageResponse response = new ConsultationMessageResponse();

        response.setId(consultationMessage.getId());

        response.setPatientId(consultationMessage.getPatient().getId());

        response.setDoctorId(consultationMessage.getDoctor().getId());

        response.setSender(consultationMessage.getSender().name());

        response.setMessage(consultationMessage.getMessage());

        response.setSentAt(consultationMessage.getSentAt());

        response.setIsRead(consultationMessage.getIsRead());

        return response;
    }

}