package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendMessageRequest {

    @NotNull(message = "Doctor Id is required")
    private Long doctorId;

    @NotBlank(message = "Message cannot be empty")
    private String message;

}