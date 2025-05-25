package com.example.medisyncbackend.Repository;

import com.example.medisyncbackend.Models.Appointment;
import com.example.medisyncbackend.Models.Appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT a FROM Appointment a WHERE DATE(a.appointmentDate) = :date")
    List<Appointment> findByAppointmentDate(@Param("date") LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE " +
            "(:id IS NULL OR a.appointmentId = :id) AND " +
            "(:patientId IS NULL OR a.patient.patientId = :patientId) AND " +
            "(:doctorId IS NULL OR a.doctor.doctorId = :doctorId) AND " +
            "(:roomId IS NULL OR a.room.roomId = :roomId) AND " +
            "(:date IS NULL OR DATE(a.appointmentDate) = :date) AND " +
            "(:status IS NULL OR a.status = :status)")
    List<Appointment> findFilteredAppointments(
            @Param("id") Integer id,
            @Param("patientId") Integer patientId,
            @Param("doctorId") Integer doctorId,
            @Param("roomId") Integer roomId,
            @Param("date") LocalDate date,
            @Param("status") AppointmentStatus status
    );

    @Query("SELECT a.billingAmount FROM Appointment a WHERE a.appointmentId = :id")
    Double getBillingAmount(@Param("id") Integer id);
}
