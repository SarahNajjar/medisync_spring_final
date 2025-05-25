package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddDoctorController {

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfSpecialization;

    @FXML
    private TextField tfContactNumber;

    @FXML
    private TextField tfPercentage;

    @FXML
    private Button btnAddDoctor;

    @FXML
    private void handleAddDoctor(ActionEvent event) {
        String sql = "INSERT INTO doctors (first_name, last_name, specialization, contact_number, percentage) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tfFirstName.getText());
            stmt.setString(2, tfLastName.getText());
            stmt.setString(3, tfSpecialization.getText());
            stmt.setString(4, tfContactNumber.getText());
            stmt.setDouble(5, Double.parseDouble(tfPercentage.getText()));
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Doctor added successfully.");
            alert.show();

            clearForm();
            ((Stage) btnAddDoctor.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setContentText("Failed to add doctor.\n" + e.getMessage());
            alert.show();
        }
    }

    private void clearForm() {
        tfFirstName.clear();
        tfLastName.clear();
        tfSpecialization.clear();
        tfContactNumber.clear();
        tfPercentage.clear();
    }
}