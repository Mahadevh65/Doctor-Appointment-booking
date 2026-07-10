package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReplyMessageRequest {

    @NotNull(message = "Patient Id is required")
    private Long patientId;

    @NotBlank(message = "Reply cannot be empty")
    private String message;

}