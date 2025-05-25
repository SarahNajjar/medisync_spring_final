package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.DBUtils;
import com.example.medisyncfrontend.Utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label lblWelcome;

    public void initialize() {
        lblWelcome.setText("Welcome, " + SessionManager.getLoggedInUser() + "!");
    }

    @FXML
    private void openPatients(ActionEvent event) {
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/patients.fxml", "Patients", null);
    }

    @FXML
    private void openDoctors(ActionEvent event) {
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/doctors.fxml", "Doctors", null);
    }

    @FXML
    private void openRooms(ActionEvent event) {
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/rooms.fxml", "Rooms", null);
    }

    @FXML
    private void openAppointments(ActionEvent event) {
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/appointments.fxml", "Appointments", null);
    }

    @FXML
    private void openWithdrawals(ActionEvent event) {
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/withdrawals.fxml", "Withdrawals", null);
    }

    @FXML
    private void logout(ActionEvent event) {
        SessionManager.clearSession();
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/Login.fxml", "Login", null);
    }
}
