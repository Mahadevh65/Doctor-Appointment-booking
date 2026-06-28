package com.hospital.management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(columnDefinition = "TEXT")
    private String medications;

    @Column(columnDefinition = "TEXT")
    private String dosageInstructions;

    @Column(columnDefinition = "TEXT")
    private String additionalNotes;

    private String followUpDate;

    @CreationTimestamp @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
