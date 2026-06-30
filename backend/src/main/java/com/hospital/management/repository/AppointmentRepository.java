package com.hospital.management.repository;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.query.Param;

// @Repository
// public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

//     List<Appointment> findByPatientOrderByCreatedAtDesc(Patient patient);
//     List<Appointment> findByDoctorOrderByAppointmentDateAscAppointmentTimeAsc(Doctor doctor);
//     List<Appointment> findByStatus(AppointmentStatus status);

//     List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);
//     List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);

//     boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatusNot(
//             Doctor doctor, LocalDate date,
//             java.time.LocalTime time, AppointmentStatus status);

//     long countByStatus(AppointmentStatus status);

//     @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDate = :date")
//     long countByAppointmentDate(LocalDate date);

//     @Query("SELECT a FROM Appointment a ORDER BY a.createdAt DESC")
//     List<Appointment> findAllOrderByCreatedAtDesc();
// }


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
    SELECT a
    FROM Appointment a
    JOIN FETCH a.patient p
    JOIN FETCH p.user
    JOIN FETCH a.doctor d
    JOIN FETCH d.user
    WHERE p = :patient
    ORDER BY a.createdAt DESC
    """)
    List<Appointment> findByPatientOrderByCreatedAtDesc(@Param("patient") Patient patient);

    @Query("""
    SELECT a
    FROM Appointment a
    JOIN FETCH a.patient p
    JOIN FETCH p.user
    JOIN FETCH a.doctor d
    JOIN FETCH d.user
    WHERE d = :doctor
    ORDER BY a.appointmentDate ASC, a.appointmentTime ASC
    """)
    List<Appointment> findByDoctorOrderByAppointmentDateAscAppointmentTimeAsc(@Param("doctor") Doctor doctor);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);

    List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);

    boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatusNot(
            Doctor doctor,
            LocalDate date,
            java.time.LocalTime time,
            AppointmentStatus status);

    long countByStatus(AppointmentStatus status);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDate = :date")
    long countByAppointmentDate(LocalDate date);

    @Query("""
    SELECT a
    FROM Appointment a
    JOIN FETCH a.patient p
    JOIN FETCH p.user
    JOIN FETCH a.doctor d
    JOIN FETCH d.user
    ORDER BY a.createdAt DESC
    """)
    List<Appointment> findAllOrderByCreatedAtDesc();
}
