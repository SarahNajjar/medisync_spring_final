package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PatientController implements Initializable {

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Integer> colId;

    @FXML
    private TableColumn<Patient, String> colFirstName;

    @FXML
    private TableColumn<Patient, String> colLastName;

    @FXML
    private TableColumn<Patient, String> colDob;

    @FXML
    private TableColumn<Patient, String> colPhone;

    @FXML
    private TextField tfFirstName, tfLastName, tfPhone, tfSearch;

    @FXML
    private DatePicker dpDob;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete;

    private ObservableList<Patient> patients = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadPatients();

        patientTable.setOnMouseClicked(e -> {
            Patient p = patientTable.getSelectionModel().getSelectedItem();
            if (p != null) {
                tfFirstName.setText(p.getFirstName());
                tfLastName.setText(p.getLastName());
                dpDob.setValue(java.time.LocalDate.parse(p.getDob()));
                tfPhone.setText(p.getPhone());
            }
        });

        tfSearch.textProperty().addListener((obs, oldText, newText) -> {
            filterPatients(newText);
        });
    }

    @FXML
    private void loadPatients() {
        patients.clear();
        try {
            ResultSet rs = DBUtils.executeQuery("SELECT * FROM patients");
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toString(),
                        rs.getString("phone_number")
                ));
            }
            patientTable.setItems(patients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterPatients(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            patientTable.setItems(patients);
            return;
        }
        ObservableList<Patient> filtered = patients.stream()
                .filter(p -> p.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                        p.getLastName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        patientTable.setItems(filtered);
    }

    @FXML
    private void handleAdd() {
        if (dpDob.getValue() == null || dpDob.getValue().isAfter(java.time.LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Date");
            alert.setHeaderText(null);
            alert.setContentText("Date of birth cannot be in the future.");
            alert.showAndWait();
            return;
        }

        String sql = "INSERT INTO patients (first_name, last_name, date_of_birth, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tfFirstName.getText());
            stmt.setString(2, tfLastName.getText());
            stmt.setDate(3, Date.valueOf(dpDob.getValue()));
            stmt.setString(4, tfPhone.getText());
            stmt.executeUpdate();
            loadPatients();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (dpDob.getValue() == null || dpDob.getValue().isAfter(java.time.LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Date");
            alert.setHeaderText(null);
            alert.setContentText("Date of birth cannot be in the future.");
            alert.showAndWait();
            return;
        }

        String sql = "UPDATE patients SET first_name=?, last_name=?, date_of_birth=?, phone_number=? WHERE patient_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tfFirstName.getText());
            stmt.setString(2, tfLastName.getText());
            stmt.setDate(3, Date.valueOf(dpDob.getValue()));
            stmt.setString(4, tfPhone.getText());
            stmt.setInt(5, selected.getId());
            stmt.executeUpdate();
            loadPatients();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "DELETE FROM patients WHERE patient_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadPatients();
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
        tfPhone.clear();
        dpDob.setValue(null);
    }

    public static class Patient {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String dob;
        private final String phone;

        public Patient(int id, String firstName, String lastName, String dob, String phone) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.phone = phone;
        }

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getDob() {
            return dob;
        }

        public String getPhone() {
            return phone;
        }
    }
}