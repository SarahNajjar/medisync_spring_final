package com.example.medisyncbackend.Services;

import com.example.medisyncbackend.Models.Withdrawal;
import com.example.medisyncbackend.Repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    // Create or Update withdrawal
    public Withdrawal saveWithdrawal(Withdrawal withdrawal) {
        return withdrawalRepository.save(withdrawal);
    }

    // Get all withdrawals
    public List<Withdrawal> getAllWithdrawals() {
        return withdrawalRepository.findAll();
    }

    // Get withdrawal by ID
    public Withdrawal getWithdrawalById(Integer withdrawalId) {
        return withdrawalRepository.findById(withdrawalId).orElse(null);
    }

    // Delete withdrawal by ID
    public boolean deleteWithdrawal(Integer withdrawalId) {
        if (withdrawalRepository.existsById(withdrawalId)) {
            withdrawalRepository.deleteById(withdrawalId);
            return true;
        }
        return false;
    }

    // Filter withdrawals by doctor, secretary, and date
    public List<Withdrawal> findFilteredWithdrawals(Integer doctorId, String secretaryUsername, java.sql.Date date) {
        return withdrawalRepository.findFilteredWithdrawals(doctorId, secretaryUsername, date);
    }
}
