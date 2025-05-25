package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RoomController implements Initializable {

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer> colId;

    @FXML
    private TableColumn<Room, String> colName;

    @FXML
    private TableColumn<Room, String> colType;

    @FXML
    private TableColumn<Room, String> colStatus;

    @FXML
    private TextField tfRoomName, tfRoomType, tfSearch;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete;

    private ObservableList<Room> rooms = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cbStatus.setItems(FXCollections.observableArrayList("Available", "Occupied", "Under Maintenance"));

        loadRooms();

        roomTable.setOnMouseClicked(e -> {
            Room r = roomTable.getSelectionModel().getSelectedItem();
            if (r != null) {
                tfRoomName.setText(r.getName());
                tfRoomType.setText(r.getType());
                cbStatus.setValue(r.getStatus());
            }
        });

        tfSearch.textProperty().addListener((obs, oldText, newText) -> filterRooms(newText));
    }

    private void loadRooms() {
        rooms.clear();
        try {
            ResultSet rs = DBUtils.executeQuery("SELECT * FROM rooms");
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getString("room_type"),
                        rs.getString("status")
                ));
            }
            roomTable.setItems(rooms);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterRooms(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            roomTable.setItems(rooms);
            return;
        }
        ObservableList<Room> filtered = rooms.stream()
                .filter(r -> r.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        r.getType().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        roomTable.setItems(filtered);
    }

    @FXML
    private void handleAdd() {
        String sql = "INSERT INTO rooms (room_name, room_type, status) VALUES (?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tfRoomName.getText());
            stmt.setString(2, tfRoomType.getText());
            stmt.setString(3, cbStatus.getValue());
            stmt.executeUpdate();
            loadRooms();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "UPDATE rooms SET room_name=?, room_type=?, status=? WHERE room_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tfRoomName.getText());
            stmt.setString(2, tfRoomType.getText());
            stmt.setString(3, cbStatus.getValue());
            stmt.setInt(4, selected.getId());
            stmt.executeUpdate();
            loadRooms();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "DELETE FROM rooms WHERE room_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadRooms();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/dashboard.fxml", "Dashboard", null);
    }

    private void clearForm() {
        tfRoomName.clear();
        tfRoomType.clear();
        cbStatus.setValue(null);
    }

    public static class Room {
        private final int id;
        private final String name;
        private final String type;
        private final String status;

        public Room(int id, String name, String type, String status) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.status = status;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getStatus() { return status; }
    }
}
