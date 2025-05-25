package com.example.medisyncbackend.Models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "withdrawals")
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer withdrawalId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate withdrawalDate;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "secretary_username", nullable = false)
    private Secretary secretary;

    public Withdrawal() {}

    public Withdrawal(Integer withdrawalId, Doctor doctor, LocalDate withdrawalDate, Double amount, Secretary secretary) {
        this.withdrawalId = withdrawalId;
        this.doctor = doctor;
        this.withdrawalDate = withdrawalDate;
        this.amount = amount;
        this.secretary = secretary;
    }

    public Integer getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(Integer withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(LocalDate withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Secretary getSecretary() {
        return secretary;
    }

    public void setSecretary(Secretary secretary) {
        this.secretary = secretary;
    }

    @Override
    public String toString() {
        return "Withdrawal{" +
                "withdrawalId=" + withdrawalId +
                ", doctor=" + doctor +
                ", withdrawalDate=" + withdrawalDate +
                ", amount=" + amount +
                ", secretary=" + secretary +
                '}';
    }
}
