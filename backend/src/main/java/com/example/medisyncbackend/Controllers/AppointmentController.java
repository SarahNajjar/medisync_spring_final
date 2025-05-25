package com.example.medisyncbackend.Controllers;

import com.example.medisyncbackend.Models.Appointment;
import com.example.medisyncbackend.Services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Integer id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Appointment saveAppointment(@RequestBody Appointment appointment) {
        return appointmentService.saveAppointment(appointment);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) {
        appointment.setAppointmentId(id);
        return appointmentService.saveAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
    }

    @GetMapping("/search")
    public List<Appointment> findFilteredAppointments(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Appointment.AppointmentStatus status) {
        return appointmentService.findFilteredAppointments(id, patientId, doctorId, roomId, date, status);
    }

    @GetMapping("/{id}/billing")
    public Double getBillingAmount(@PathVariable Integer id) {
        return appointmentService.getBillingAmount(id);
    }
}
