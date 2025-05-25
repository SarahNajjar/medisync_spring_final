package com.example.medisyncbackend.Services;

import com.example.medisyncbackend.Models.Patient;
import com.example.medisyncbackend.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Create or Update patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get patient by ID
    public Patient getPatientById(Integer patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    // Delete patient by ID
    public boolean deletePatient(Integer patientId) {
        if (patientRepository.existsById(patientId)) {
            patientRepository.deleteById(patientId);
            return true;
        }
        return false;
    }

    // Filter patients by optional fields
    public List<Patient> findFilteredPatients(Integer id, String firstName, String lastName, LocalDate dob, String phoneNumber) {
        return patientRepository.findFilteredPatients(id, firstName, lastName, dob, phoneNumber);
    }
}
