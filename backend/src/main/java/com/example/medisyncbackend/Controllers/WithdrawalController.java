package com.example.medisyncbackend.Controllers;

import com.example.medisyncbackend.Models.Withdrawal;
import com.example.medisyncbackend.Services.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @GetMapping
    public List<Withdrawal> getAllWithdrawals() {
        return withdrawalService.getAllWithdrawals();
    }

    @GetMapping("/{id}")
    public Withdrawal getWithdrawalById(@PathVariable Integer id) {
        return withdrawalService.getWithdrawalById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Withdrawal saveWithdrawal(@RequestBody Withdrawal withdrawal) {
        return withdrawalService.saveWithdrawal(withdrawal);
    }

    @PutMapping("/{id}")
    public Withdrawal updateWithdrawal(@PathVariable Integer id, @RequestBody Withdrawal withdrawal) {
        withdrawal.setWithdrawalId(id);
        return withdrawalService.saveWithdrawal(withdrawal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWithdrawal(@PathVariable Integer id) {
        withdrawalService.deleteWithdrawal(id);
    }

    @GetMapping("/search")
    public List<Withdrawal> findFilteredWithdrawals(
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) String secretaryUsername,
            @RequestParam(required = false) java.sql.Date date) {
        return withdrawalService.findFilteredWithdrawals(doctorId, secretaryUsername, date);
    }
}
