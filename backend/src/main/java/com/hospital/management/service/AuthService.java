package com.hospital.management.service;

import com.hospital.management.dto.request.LoginRequest;
import com.hospital.management.dto.request.RegisterRequest;
import com.hospital.management.dto.response.JwtResponse;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.entity.enums.ERole;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.RoleRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.security.jwt.JwtUtils;
import com.hospital.management.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.hospital.management.dto.request.DoctorRegisterRequest;
import com.hospital.management.dto.request.PatientRegisterRequest;

import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;

import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .roles(roles)
                .build();
    }

    @Transactional
    public MessageResponse registerDoctor(DoctorRegisterRequest req) {

        // Validation
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        // Create User
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .phone(req.getPhone())
                .enabled(true)
                .build();

        // Assign Role
        Set<Role> roles = new HashSet<>();
        roles.add(findRole(ERole.ROLE_DOCTOR));
        user.setRoles(roles);

        // Save User
        User savedUser = userRepository.save(user);

        // Create Doctor
        Doctor doctor = Doctor.builder()
                .user(savedUser)
                .specialization(req.getSpecialization())
                .qualification(req.getQualification())
                .licenseNumber(req.getLicenseNumber())
                .experienceYears(req.getExperienceYears())
                .department(req.getDepartment())
                .bio(req.getBio())
                .consultationFee(req.getConsultationFee())
                .availableDays(req.getAvailableDays())
                .availableTimeStart(req.getAvailableTimeStart())
                .availableTimeEnd(req.getAvailableTimeEnd())
                .active(true)
                .build();

        // Save Doctor
        doctorRepository.save(doctor);

        return new MessageResponse("Doctor registered successfully!");
    }

    @Transactional
    public MessageResponse registerPatient(PatientRegisterRequest req) {

        // Check Username
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        // Check Email
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        // Create User
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .phone(req.getPhone())
                .enabled(true)
                .build();

        // Assign Patient Role
        Set<Role> roles = new HashSet<>();
        roles.add(findRole(ERole.ROLE_PATIENT));
        user.setRoles(roles);

        // Save User
        User savedUser = userRepository.save(user);

        // Create Patient
        Patient patient = Patient.builder()
                .user(savedUser)
                .dateOfBirth(req.getDateOfBirth())
                .gender(req.getGender())
                .bloodGroup(req.getBloodGroup())
                .address(req.getAddress())
                .city(req.getCity())
                .state(req.getState())
                .pincode(req.getPincode())
                .emergencyContactName(req.getEmergencyContactName())
                .emergencyContactPhone(req.getEmergencyContactPhone())
                .allergies(req.getAllergies())
                .medicalHistory(req.getMedicalHistory())
                .build();

        // Save Patient
        patientRepository.save(patient);

        return new MessageResponse("Patient registered successfully!");
    }
    // @Transactional
    // public MessageResponse registerUser(RegisterRequest req) {
    // if (userRepository.existsByUsername(req.getUsername()))
    // throw new IllegalArgumentException("Username is already taken!");
    // if (userRepository.existsByEmail(req.getEmail()))
    // throw new IllegalArgumentException("Email is already in use!");

    // User user = User.builder()
    // .username(req.getUsername())
    // .email(req.getEmail())
    // .password(encoder.encode(req.getPassword()))
    // .firstName(req.getFirstName())
    // .lastName(req.getLastName())
    // .phone(req.getPhone())
    // .build();

    // Set<String> strRoles = req.getRoles();
    // Set<Role> roles = new HashSet<>();

    // if (strRoles == null || strRoles.isEmpty()) {
    // roles.add(findRole(ERole.ROLE_PATIENT));
    // } else {
    // for (String role : strRoles) {
    // switch (role.toLowerCase()) {
    // case "admin" -> roles.add(findRole(ERole.ROLE_ADMIN));
    // case "doctor" -> roles.add(findRole(ERole.ROLE_DOCTOR));
    // default -> roles.add(findRole(ERole.ROLE_PATIENT));
    // }
    // }
    // }
    // user.setRoles(roles);
    // userRepository.save(user);
    // return new MessageResponse("User registered successfully!");
    // }

    private Role findRole(ERole roleEnum) {
        return roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role not found: " + roleEnum.name()));
    }
}
