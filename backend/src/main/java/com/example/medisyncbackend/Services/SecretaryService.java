package com.example.medisyncbackend.Services;

import com.example.medisyncbackend.Models.Secretary;
import com.example.medisyncbackend.Repository.SecretaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretaryService {

    @Autowired
    private SecretaryRepository secretaryRepository;

    // Create or Update secretary
    public Secretary saveSecretary(Secretary secretary) {
        return secretaryRepository.save(secretary);
    }

    // Get all secretaries
    public List<Secretary> getAllSecretaries() {
        return secretaryRepository.findAll();
    }

    // Get secretary by username
    public Secretary getSecretaryByUsername(String username) {
        return secretaryRepository.findById(username).orElse(null);
    }

    // Delete secretary by username
    public boolean deleteSecretary(String username) {
        if (secretaryRepository.existsById(username)) {
            secretaryRepository.deleteById(username);
            return true;
        }
        return false;
    }

    // Secretary login authentication
    public boolean authenticateSecretary(String username, String password) {
        Secretary sec = secretaryRepository.findByUsernameAndPassword(username, password);
        System.out.println("Repository returned: " + sec);
        return sec != null;
    }

}
