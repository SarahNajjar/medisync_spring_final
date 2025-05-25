package com.example.medisyncfrontend.Controllers;

import com.example.medisyncbackend.Models.Appointment;
import com.example.medisyncfrontend.Utils.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;
import java.util.List;

public class AppointmentController {

    @FXML
    private TableView<Appointment> tableAppointments;

    @FXML
    private TableColumn<Appointment, Integer> colId;

    @FXML
    private TableColumn<Appointment, String> colDate;

    @FXML
    private TableColumn<Appointment, String> colStatus;

    @FXML
    private TableColumn<Appointment, Double> colBilling;

    @FXML
    private TableColumn<Appointment, String> colStatusDate;

    @FXML
    private TableColumn<Appointment, Integer> colPatientId;

    @FXML
    private TableColumn<Appointment, Integer> colDoctorId;

    @FXML
    private TableColumn<Appointment, Integer> colRoomId;

    @FXML
    private TableColumn<Appointment, String> colSecretary;

    @FXML
    private Button btnRefresh;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAppointmentId()).asObject());
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAppointmentDate().toString()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().toString()));
        colBilling.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getBillingAmount()).asObject());
        colStatusDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatusUpdateDate().toString()));
        colPatientId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getPatient().getPatientId()).asObject());
        colDoctorId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getDoctor().getDoctorId()).asObject());
        colRoomId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getRoom().getRoomId()).asObject());
        colSecretary.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSecretary().getUsername()));

        loadAppointments();
    }

    @FXML
    void loadAppointments() {
        String response = ApiClient.get("/api/appointments");
        if (response != null) {
            try {
                List<Appointment> appointments = objectMapper.readValue(response, new TypeReference<>() {});
                ObservableList<Appointment> data = FXCollections.observableArrayList(appointments);
                tableAppointments.setItems(data);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to parse appointment data.");
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
