package com.hospital.management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Size(max = 100) @Email
    private String email;

    @NotBlank @Size(min = 6, max = 40)
    private String password;

    @NotBlank @Size(max = 50)
    private String firstName;

    @NotBlank @Size(max = 50)
    private String lastName;

    @Size(max = 15)
    private String phone;

    // "admin", "doctor", or "patient"
    private Set<String> roles;
}
