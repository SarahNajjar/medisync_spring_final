package com.example.medisyncfrontend.Controllers;

import com.example.medisyncbackend.Models.Withdrawal;
import com.example.medisyncfrontend.Utils.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;
import java.util.List;

public class WithdrawalController {

    @FXML
    private TableView<Withdrawal> tableWithdrawals;

    @FXML
    private TableColumn<Withdrawal, Integer> colId;

    @FXML
    private TableColumn<Withdrawal, String> colDate;

    @FXML
    private TableColumn<Withdrawal, Double> colAmount;

    @FXML
    private TableColumn<Withdrawal, Integer> colDoctorId;

    @FXML
    private TableColumn<Withdrawal, String> colSecretary;

    @FXML
    private Button btnRefresh;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getWithdrawalId()).asObject());
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getWithdrawalDate().toString()));
        colAmount.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getAmount()).asObject());
        colDoctorId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getDoctor().getDoctorId()).asObject());
        colSecretary.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSecretary().getUsername()));

        loadWithdrawals();
    }

    @FXML
    void loadWithdrawals() {
        String response = ApiClient.get("/api/withdrawals");
        if (response != null) {
            try {
                List<Withdrawal> withdrawals = objectMapper.readValue(response, new TypeReference<>() {});
                ObservableList<Withdrawal> data = FXCollections.observableArrayList(withdrawals);
                tableWithdrawals.setItems(data);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to parse withdrawal data.");
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
