package com.example.medisyncbackend.Services;
import com.example.medisyncbackend.Models.Doctor;
import com.example.medisyncbackend.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Create or Update doctor
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Get doctor by ID
    public Doctor getDoctorById(Integer doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    // Delete doctor by ID
    public boolean deleteDoctor(Integer doctorId) {
        if (doctorRepository.existsById(doctorId)) {
            doctorRepository.deleteById(doctorId);
            return true;
        }
        return false;
    }

    // Get doctor percentage (business logic)
    public Double getDoctorPercentage(Integer doctorId) {
        return doctorRepository.getDoctorPercentage(doctorId);
    }

    // Filter doctors by optional fields
    public List<Doctor> findFilteredDoctors(Integer id, String firstName, String lastName, String specialization, String contact) {
        return doctorRepository.findFilteredDoctors(id, firstName, lastName, specialization, contact);
    }
}
