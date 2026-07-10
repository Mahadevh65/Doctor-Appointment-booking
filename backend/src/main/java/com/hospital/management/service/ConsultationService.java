package com.hospital.management.service;

import com.hospital.management.dto.request.ReplyMessageRequest;
import com.hospital.management.dto.request.SendMessageRequest;
import com.hospital.management.dto.response.ConsultationMessageResponse;
import com.hospital.management.entity.ConsultationMessage;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.enums.Sender;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.ConsultationMessageRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {

        private final ConsultationMessageRepository consultationMessageRepository;
        private final PatientRepository patientRepository;
        private final DoctorRepository doctorRepository;

        // =====================================================
        // Patient sends message to Doctor
        // =====================================================

        @Transactional
        public ConsultationMessageResponse sendMessage(Long userId,
                        SendMessageRequest request) {

                Patient patient = patientRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

                Doctor doctor = doctorRepository.findById(request.getDoctorId())
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                ConsultationMessage message = ConsultationMessage.builder()
                                .patient(patient)
                                .doctor(doctor)
                                .sender(Sender.PATIENT)
                                .message(request.getMessage())
                                .build();

                return ConsultationMessageResponse.from(
                                consultationMessageRepository.save(message));
        }

        // =====================================================
        // Doctor replies
        // =====================================================

        @Transactional
        public ConsultationMessageResponse replyMessage(Long userId,
                        ReplyMessageRequest request) {

                Doctor doctor = doctorRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                Patient patient = patientRepository.findById(request.getPatientId())
                                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

                ConsultationMessage message = ConsultationMessage.builder()
                                .doctor(doctor)
                                .patient(patient)
                                .sender(Sender.DOCTOR)
                                .message(request.getMessage())
                                .build();

                return ConsultationMessageResponse.from(
                                consultationMessageRepository.save(message));
        }

        // =====================================================
        // Complete conversation
        // =====================================================

        public List<ConsultationMessageResponse> getConversation(Long patientId,
                        Long doctorId) {

                Patient patient = patientRepository.findById(patientId)
                                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                return consultationMessageRepository
                                .findByPatientAndDoctorOrderBySentAtAsc(patient, doctor)
                                .stream()
                                .map(ConsultationMessageResponse::from)
                                .toList();
        }

        // =====================================================
        // Patient Message History
        // =====================================================

        public List<ConsultationMessageResponse> getPatientMessages(Long userId) {

                Patient patient = patientRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

                return consultationMessageRepository
                                .findByPatientOrderBySentAtDesc(patient)
                                .stream()
                                .map(ConsultationMessageResponse::from)
                                .toList();
        }

        // =====================================================
        // Doctor Message History
        // =====================================================

        public List<ConsultationMessageResponse> getDoctorMessages(Long userId) {

                Doctor doctor = doctorRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                return consultationMessageRepository
                                .findByDoctorOrderBySentAtDesc(doctor)
                                .stream()
                                .map(ConsultationMessageResponse::from)
                                .toList();
        }

        // Paitient Conversation With Doctor new Method
        public List<ConsultationMessageResponse> getPatientConversation(

                        Long userId,

                        Long doctorId) {

                Patient patient = patientRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                return consultationMessageRepository

                                .findByPatientAndDoctorOrderBySentAtAsc(
                                                patient,
                                                doctor)

                                .stream()

                                .map(ConsultationMessageResponse::from)

                                .toList();

        }

        public List<ConsultationMessageResponse> getDoctorConversation(
                        Long userId,
                        Long patientId) {

                Doctor doctor = doctorRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                Patient patient = patientRepository.findById(patientId)
                                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

                return consultationMessageRepository
                                .findByPatientAndDoctorOrderBySentAtAsc(patient, doctor)
                                .stream()
                                .map(ConsultationMessageResponse::from)
                                .toList();
        }

}