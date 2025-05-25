package com.example.medisyncbackend.Repository;

import com.example.medisyncbackend.Models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    // Search by filters with optional parameters
    @Query("SELECT p FROM Patient p WHERE " +
            "(:id IS NULL OR p.patientId = :id) AND " +
            "(:firstName IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:dob IS NULL OR p.dateOfBirth = :dob) AND " +
            "(:phone IS NULL OR p.phoneNumber LIKE CONCAT('%', :phone, '%'))")
    List<Patient> findFilteredPatients(
            @Param("id") Integer id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("dob") LocalDate dob,
            @Param("phone") String phone
    );

}
