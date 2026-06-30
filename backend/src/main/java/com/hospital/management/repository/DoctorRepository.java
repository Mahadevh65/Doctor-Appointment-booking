package com.hospital.management.repository;

import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.hospital.management.dto.response.DoctorResponse;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
        Optional<Doctor> findByUser(User user);

        Optional<Doctor> findByUserId(Long userId);

        List<Doctor> findByActiveTrue();

        List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

        @Query("SELECT d FROM Doctor d WHERE d.active = true AND " +
                        "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(d.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(d.user.lastName)  LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Doctor> searchDoctors(String keyword);

        long countByActiveTrue();

        @Query("""
                        SELECT
                            d.id,
                            u.id,
                            u.firstName,
                            u.lastName,
                            u.email,
                            u.phone,
                            d.specialization,
                            d.qualification,
                            d.licenseNumber,
                            d.experienceYears,
                            d.department,
                            d.bio,
                            d.consultationFee,
                            d.availableDays,
                            d.availableTimeStart,
                            d.availableTimeEnd,
                            d.active
                        FROM Doctor d
                        JOIN d.user u
                        """)
        List<Object[]> getDoctorDetails();

        @Query("""
                        SELECT d
                        FROM Doctor d
                        JOIN FETCH d.user
                        WHERE d.user.id = :userId
                        """)
        Optional<Doctor> findProfileByUserId(Long userId);

        @Query("""
                        SELECT
                            d.id,
                            u.id,
                            u.firstName,
                            u.lastName,
                            u.email,
                            u.phone,
                            d.specialization,
                            d.qualification,
                            d.licenseNumber,
                            d.experienceYears,
                            d.department,
                            d.bio,
                            d.consultationFee,
                            d.availableDays,
                            d.availableTimeStart,
                            d.availableTimeEnd,
                            d.active
                        FROM Doctor d
                        JOIN d.user u
                        WHERE d.active = true
                        """)
        List<Object[]> getActiveDoctorDetails();

        @Query("""
                        SELECT
                            d.id,
                            u.id,
                            u.firstName,
                            u.lastName,
                            u.email,
                            u.phone,
                            d.specialization,
                            d.qualification,
                            d.licenseNumber,
                            d.experienceYears,
                            d.department,
                            d.bio,
                            d.consultationFee,
                            d.availableDays,
                            d.availableTimeStart,
                            d.availableTimeEnd,
                            d.active
                        FROM Doctor d
                        JOIN d.user u
                        WHERE d.active = true
                        AND (
                        LOWER(d.specialization) LIKE LOWER(CONCAT('%',:keyword,'%'))
                        OR LOWER(u.firstName) LIKE LOWER(CONCAT('%',:keyword,'%'))
                        OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:keyword,'%'))
                        )
                        """)
        List<Object[]> searchDoctorDetails(String keyword);
}
