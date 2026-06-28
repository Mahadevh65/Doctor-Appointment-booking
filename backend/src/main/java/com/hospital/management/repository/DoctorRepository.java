package com.hospital.management.repository;

import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
}
