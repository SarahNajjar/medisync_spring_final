package com.example.medisyncbackend.Services;

import com.example.medisyncbackend.Models.Appointment;
import com.example.medisyncbackend.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Create or Update appointment
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Appointment getAppointmentById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }

    // Delete appointment by ID
    public boolean deleteAppointment(Integer appointmentId) {
        if (appointmentRepository.existsById(appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
            return true;
        }
        return false;
    }

    // Get appointments by date
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }

    // Filter appointments by multiple fields
    public List<Appointment> findFilteredAppointments(Integer id, Integer patientId, Integer doctorId, Integer roomId, LocalDate date, Appointment.AppointmentStatus status) {
        return appointmentRepository.findFilteredAppointments(id, patientId, doctorId, roomId, date, status);
    }

    public Double getBillingAmount(Integer appointmentId) {
        return appointmentRepository.getBillingAmount(appointmentId);
    }
}
