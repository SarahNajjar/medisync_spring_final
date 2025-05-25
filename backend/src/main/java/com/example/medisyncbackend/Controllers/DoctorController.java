package com.example.medisyncbackend.Controllers;

import com.example.medisyncbackend.Models.Doctor;
import com.example.medisyncbackend.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Integer id) {
        return doctorService.getDoctorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor saveDoctor(@RequestBody Doctor doctor) {
        return doctorService.saveDoctor(doctor);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable Integer id, @RequestBody Doctor doctor) {
        doctor.setDoctorId(id);
        return doctorService.saveDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable Integer id) {
        doctorService.deleteDoctor(id);
    }

    @GetMapping("/search")
    public List<Doctor> findFilteredDoctors(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String contact) {
        return doctorService.findFilteredDoctors(id, firstName, lastName, specialization, contact);
    }

    @GetMapping("/{id}/percentage")
    public Double getDoctorPercentage(@PathVariable Integer id) {
        return doctorService.getDoctorPercentage(id);
    }
}
