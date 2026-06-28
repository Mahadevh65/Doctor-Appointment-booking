package com.hospital.management.config;

import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.entity.enums.ERole;
import com.hospital.management.repository.RoleRepository;
import com.hospital.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        for (ERole eRole : ERole.values()) {
            if (roleRepository.findByName(eRole).isEmpty()) {
                roleRepository.save(new Role(null, eRole));
                log.info("Seeded role: {}", eRole);
            }
        }
    }

    private void seedAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow();
            User admin = User.builder()
                    .username("admin")
                    .email("admin@hospital.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .firstName("System")
                    .lastName("Admin")
                    .phone("9999999999")
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
            log.info("Default admin created: username=admin, password=Admin@123");
        }
    }
}
