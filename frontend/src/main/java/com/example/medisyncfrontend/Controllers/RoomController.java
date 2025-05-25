package com.example.medisyncfrontend.Controllers;

import com.example.medisyncbackend.Models.Room;
import com.example.medisyncfrontend.Utils.ApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class RoomController {

    @FXML
    private TableView<Room> tableRooms;

    @FXML
    private TableColumn<Room, Integer> colId;

    @FXML
    private TableColumn<Room, String> colName;

    @FXML
    private TableColumn<Room, String> colType;

    @FXML
    private TableColumn<Room, String> colStatus;

    @FXML
    private Button btnRefresh;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getRoomId()).asObject());
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRoomName()));
        colType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRoomType()));
        colStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty());

        loadRooms();
    }

    @FXML
    void loadRooms() {
        String response = ApiClient.get("/api/rooms");

        if (response != null) {
            try {
                List<Room> rooms = objectMapper.readValue(response, new TypeReference<>() {});
                ObservableList<Room> data = FXCollections.observableArrayList(rooms);
                tableRooms.setItems(data);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Failed to parse room data.");
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
