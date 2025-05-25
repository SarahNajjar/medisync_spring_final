package com.example.medisyncfrontend.Controllers;

import com.example.medisyncbackend.Models.Doctor;
import com.example.medisyncfrontend.Utils.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class DoctorController {

    @FXML private TableView<Doctor> tableDoctors;
    @FXML private TableColumn<Doctor, Integer> colId;
    @FXML private TableColumn<Doctor, String> colFirstName;
    @FXML private TableColumn<Doctor, String> colLastName;
    @FXML private TableColumn<Doctor, String> colSpecialization;
    @FXML private TableColumn<Doctor, String> colPhone;
    @FXML private TableColumn<Doctor, Double> colPercentage;
    @FXML private Button btnRefresh;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue().getDoctorId()));

        colFirstName.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getFirstName()));

        colLastName.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getLastName()));

        colSpecialization.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getSpecialization()));

        colPhone.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getContactNumber()));

        colPercentage.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyObjectWrapper<>(cell.getValue().getPercentage()));

        loadDoctors();
    }

    @FXML
    void loadDoctors() {
        String response = ApiClient.get("/api/doctors");

        if (response != null) {
            try {
                List<Doctor> doctors = objectMapper.readValue(response, new TypeReference<>() {});
                ObservableList<Doctor> data = FXCollections.observableArrayList(doctors);
                tableDoctors.setItems(data);
            } catch (IOException e) {
                showError("Failed to parse doctor data.");
                e.printStackTrace();
            }
        } else {
            showError("Failed to connect to backend.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
