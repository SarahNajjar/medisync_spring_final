package com.example.medisyncbackend.Repository;

import com.example.medisyncbackend.Models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query("SELECT d FROM Doctor d WHERE " +
            "(:id IS NULL OR d.doctorId = :id) AND " +
            "(:firstName IS NULL OR LOWER(d.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:specialization IS NULL OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))) AND " +
            "(:contact IS NULL OR d.contactNumber LIKE CONCAT('%', :contact, '%'))")
    List<Doctor> findFilteredDoctors(
            @Param("id") Integer id,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("specialization") String specialization,
            @Param("contact") String contact
    );

    @Query("SELECT d.percentage FROM Doctor d WHERE d.doctorId = :id")
    Double getDoctorPercentage(@Param("id") Integer id);

}
