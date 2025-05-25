package com.example.medisyncfrontend.Controllers;

import com.example.medisyncbackend.Models.Doctor;
import com.example.medisyncfrontend.Utils.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AddDoctorController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField specializationField;
    @FXML private TextField contactNumberField;
    @FXML private TextField percentageField;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private void handleSave() {
        try {
            Doctor doctor = new Doctor();
            doctor.setFirstName(firstNameField.getText());
            doctor.setLastName(lastNameField.getText());
            doctor.setSpecialization(specializationField.getText());
            doctor.setContactNumber(contactNumberField.getText());
            doctor.setPercentage(Double.parseDouble(percentageField.getText()));

            String json = objectMapper.writeValueAsString(doctor);
            String response = ApiClient.post("/api/doctors", json);

            if (response != null) {
                showAlert("Doctor added successfully!", Alert.AlertType.INFORMATION);
                closeWindow();
            } else {
                showAlert("Failed to add doctor. Backend error.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Invalid input or server error.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
