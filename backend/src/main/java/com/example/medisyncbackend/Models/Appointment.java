package com.example.medisyncbackend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @ManyToOne @JoinColumn(name = "patient_id",  nullable = false)
    private Patient patient;

    @ManyToOne @JoinColumn(name = "doctor_id",   nullable = false)
    private Doctor doctor;

    /** Pair with @JsonManagedReference in Room */
    @JsonBackReference
    @ManyToOne @JoinColumn(name = "room_id",     nullable = false)
    private Room room;

    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private Double billingAmount;
    private LocalDate statusUpdateDate;

    @ManyToOne @JoinColumn(name = "secretary_username", nullable = false)
    private Secretary secretary;

    public enum AppointmentStatus { Scheduled, Completed, Canceled }

    /* ───────────── getters / setters (unchanged) ───────────── */

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public Double getBillingAmount() { return billingAmount; }
    public void setBillingAmount(Double billingAmount) { this.billingAmount = billingAmount; }

    public LocalDate getStatusUpdateDate() { return statusUpdateDate; }
    public void setStatusUpdateDate(LocalDate statusUpdateDate) { this.statusUpdateDate = statusUpdateDate; }

    public Secretary getSecretary() { return secretary; }
    public void setSecretary(Secretary secretary) { this.secretary = secretary; }

    /* helper IDs for the frontend */
    public Integer getPatientId()   { return patient   != null ? patient.getPatientId()   : null; }
    public Integer getDoctorId()    { return doctor    != null ? doctor.getDoctorId()    : null; }
    public Integer getRoomId()      { return room      != null ? room.getRoomId()        : null; }
    public String  getSecretaryUsername() { return secretary != null ? secretary.getUsername() : null; }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentDate=" + appointmentDate +
                ", status=" + status +
                ", billingAmount=" + billingAmount +
                '}';
    }
}
