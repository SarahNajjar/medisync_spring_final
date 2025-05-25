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

public class DoctorController implements Initializable {

    @FXML
    private TableView<Doctor> doctorTable;

    @FXML
    private TableColumn<Doctor, Integer> colId;

    @FXML
    private TableColumn<Doctor, String> colFirstName;

    @FXML
    private TableColumn<Doctor, String> colLastName;

    @FXML
    private TableColumn<Doctor, String> colSpecialization;

    @FXML
    private TableColumn<Doctor, String> colContact;

    @FXML
    private TableColumn<Doctor, Double> colPercentage;

    @FXML
    private TextField tfFirstName, tfLastName, tfSpecialization, tfContact, tfSearch;

    @FXML
    private TextField tfPercentage;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete;

    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));

        loadDoctors();

        doctorTable.setOnMouseClicked(e -> {
            Doctor d = doctorTable.getSelectionModel().getSelectedItem();
            if (d != null) {
                tfFirstName.setText(d.getFirstName());
                tfLastName.setText(d.getLastName());
                tfSpecialization.setText(d.getSpecialization());
                tfContact.setText(d.getContact());
                tfPercentage.setText(String.valueOf(d.getPercentage()));
            }
        });

        tfSearch.textProperty().addListener((obs, oldText, newText) -> filterDoctors(newText));
    }

    @FXML
    private void loadDoctors() {
        doctors.clear();
        try {
            ResultSet rs = DBUtils.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("doctor_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("specialization"),
                        rs.getString("contact_number"),
                        rs.getDouble("percentage")
                ));
            }
            doctorTable.setItems(doctors);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterDoctors(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            doctorTable.setItems(doctors);
            return;
        }
        ObservableList<Doctor> filtered = doctors.stream()
                .filter(d -> d.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                        d.getLastName().toLowerCase().contains(keyword.toLowerCase()) ||
                        d.getSpecialization().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        doctorTable.setItems(filtered);
    }

    @FXML
    private void handleAdd() {
        String sql = "INSERT INTO doctors (first_name, last_name, specialization, contact_number, percentage) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tfFirstName.getText());
            stmt.setString(2, tfLastName.getText());
            stmt.setString(3, tfSpecialization.getText());
            stmt.setString(4, tfContact.getText());
            stmt.setDouble(5, Double.parseDouble(tfPercentage.getText()));
            stmt.executeUpdate();
            loadDoctors();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "UPDATE doctors SET first_name=?, last_name=?, specialization=?, contact_number=?, percentage=? WHERE doctor_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tfFirstName.getText());
            stmt.setString(2, tfLastName.getText());
            stmt.setString(3, tfSpecialization.getText());
            stmt.setString(4, tfContact.getText());
            stmt.setDouble(5, Double.parseDouble(tfPercentage.getText()));
            stmt.setInt(6, selected.getId());
            stmt.executeUpdate();
            loadDoctors();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "DELETE FROM doctors WHERE doctor_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadDoctors();
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
        tfFirstName.clear();
        tfLastName.clear();
        tfSpecialization.clear();
        tfContact.clear();
        tfPercentage.clear();
    }

    public static class Doctor {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String specialization;
        private final String contact;
        private final double percentage;

        public Doctor(int id, String firstName, String lastName, String specialization, String contact, double percentage) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.specialization = specialization;
            this.contact = contact;
            this.percentage = percentage;
        }

        public int getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getSpecialization() { return specialization; }
        public String getContact() { return contact; }
        public double getPercentage() { return percentage; }
    }
}
