package com.example.medisyncfrontend.Controllers;

import com.example.medisyncbackend.Models.Patient;
import com.example.medisyncfrontend.Utils.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class PatientController {

    @FXML private TableView<Patient> tablePatients;

    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String>  colFirstName;
    @FXML private TableColumn<Patient, String>  colLastName;
    @FXML private TableColumn<Patient, String>  colPhone;
    @FXML private TableColumn<Patient, String>  colDob;

    @FXML private Button btnRefresh;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {

        // --- column → property bindings ------------------------------------
        colId.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getPatientId()));

        colFirstName.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getFirstName()));

        colLastName.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getLastName()));

        colPhone.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getPhoneNumber()));

        colDob.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getDateOfBirth().toString()));
        // --------------------------------------------------------------------

        loadPatients();
    }

    @FXML
    private void loadPatients() {
        String response = ApiClient.get("/api/patients");

        if (response != null) {
            try {
                List<Patient> patients =
                        objectMapper.readValue(response, new TypeReference<>() {});
                ObservableList<Patient> data = FXCollections.observableArrayList(patients);
                tablePatients.setItems(data);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to parse patient data.");
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
