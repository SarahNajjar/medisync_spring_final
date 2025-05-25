package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DashboardController {

    @FXML
    private Button btnPatients;

    @FXML
    private Button btnDoctors;

    @FXML
    private Button btnAppointments;

    @FXML
    private Button btnRooms;

    @FXML
    private Button btnWithdrawals;

    @FXML
    private Button btnLogout;

    @FXML
    public void initialize() {
        // Optional: setup logic
    }

    @FXML
    void goToPatients() {
        SceneSwitcher.switchTo("/com/example/medisyncfrontend/patients.fxml", "Patients");

    }

    @FXML
    void goToDoctors() {
        SceneSwitcher.switchTo("/com/example/medisyncfrontend/doctors.fxml", "Doctors");
    }

    @FXML
    void goToAppointments() {
        SceneSwitcher.switchTo("/com/example/medisyncfrontend/appointments.fxml", "Appointments");
    }

    @FXML
    void goToRooms() {
        SceneSwitcher.switchTo("/com/example/medisyncfrontend/rooms.fxml", "Rooms");
    }

    @FXML
    void goToWithdrawals() {
        SceneSwitcher.switchTo("/com/example/medisyncfrontend/withdrawals.fxml", "Withdrawals");
    }

    @FXML
    void logout() {
        SceneSwitcher.switchTo("/com/example/medisyncfrontend/login.fxml", "Login");
    }
}
