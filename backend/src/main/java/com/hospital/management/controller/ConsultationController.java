package com.hospital.management.controller;

import com.hospital.management.dto.request.ReplyMessageRequest;
import com.hospital.management.dto.request.SendMessageRequest;
import com.hospital.management.dto.response.ConsultationMessageResponse;
import com.hospital.management.security.services.UserDetailsImpl;
import com.hospital.management.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ConsultationController {

        private final ConsultationService consultationService;

        // =====================================================
        // Patient sends message
        // =====================================================

        @PostMapping("/patient/send")
        @PreAuthorize("hasRole('PATIENT')")
        public ResponseEntity<ConsultationMessageResponse> sendMessage(

                        @AuthenticationPrincipal UserDetailsImpl userDetails,

                        @Valid @RequestBody SendMessageRequest request) {

                return ResponseEntity.ok(
                                consultationService.sendMessage(userDetails.getId(), request));
        }

        // =====================================================
        // Doctor replies
        // =====================================================

        @PostMapping("/doctor/reply")
        @PreAuthorize("hasRole('DOCTOR')")
        public ResponseEntity<ConsultationMessageResponse> replyMessage(

                        @AuthenticationPrincipal UserDetailsImpl userDetails,

                        @Valid @RequestBody ReplyMessageRequest request) {

                return ResponseEntity.ok(
                                consultationService.replyMessage(userDetails.getId(), request));
        }

        // =====================================================
        // Patient Message History
        // =====================================================

        @GetMapping("/patient")
        @PreAuthorize("hasRole('PATIENT')")
        public ResponseEntity<List<ConsultationMessageResponse>> getPatientMessages(

                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

                return ResponseEntity.ok(
                                consultationService.getPatientMessages(userDetails.getId()));
        }

        // =====================================================
        // Doctor Message History
        // =====================================================

        @GetMapping("/doctor")
        @PreAuthorize("hasRole('DOCTOR')")
        public ResponseEntity<List<ConsultationMessageResponse>> getDoctorMessages(

                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

                return ResponseEntity.ok(
                                consultationService.getDoctorMessages(userDetails.getId()));
        }

        // =====================================================
        // Complete Conversation
        // =====================================================

        @GetMapping("/conversation")
        @PreAuthorize("hasRole('PATIENT')")
        public ResponseEntity<List<ConsultationMessageResponse>> getConversation(

                        @AuthenticationPrincipal UserDetailsImpl userDetails,

                        @RequestParam Long doctorId) {

                return ResponseEntity.ok(
                                consultationService.getPatientConversation(
                                                userDetails.getId(),
                                                doctorId));
        }

        @GetMapping("/doctor/conversation")
        @PreAuthorize("hasRole('DOCTOR')")
        public ResponseEntity<List<ConsultationMessageResponse>> getDoctorConversation(

                        @AuthenticationPrincipal UserDetailsImpl userDetails,

                        @RequestParam Long patientId) {

                return ResponseEntity.ok(
                                consultationService.getDoctorConversation(
                                                userDetails.getId(),
                                                patientId));
        }

}