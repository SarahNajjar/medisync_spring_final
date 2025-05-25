package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.DBUtils;
import com.example.medisyncfrontend.Utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> colId;
    @FXML private TableColumn<Appointment, Integer> colPatientId;
    @FXML private TableColumn<Appointment, Integer> colDoctorId;
    @FXML private TableColumn<Appointment, Integer> colRoomId;
    @FXML private TableColumn<Appointment, String> colDate;
    @FXML private TableColumn<Appointment, String> colStatus;
    @FXML private TableColumn<Appointment, Double> colBillingAmount;
    @FXML private TableColumn<Appointment, String> colStatusUpdateDate;
    @FXML private TableColumn<Appointment, String> colSecretary;

    @FXML private TextField tfPatientId, tfDoctorId, tfRoomId, tfBillingAmount;
    @FXML private DatePicker dpAppointmentDate;
    @FXML private ComboBox<String> cbStatus;
    @FXML private Button btnAdd, btnUpdate, btnDelete;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colBillingAmount.setCellValueFactory(new PropertyValueFactory<>("billingAmount"));
        colStatusUpdateDate.setCellValueFactory(new PropertyValueFactory<>("statusUpdateDate"));
        colSecretary.setCellValueFactory(new PropertyValueFactory<>("secretary"));

        cbStatus.setItems(FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled"));

        loadAppointments();

        appointmentTable.setOnMouseClicked(e -> {
            Appointment a = appointmentTable.getSelectionModel().getSelectedItem();
            if (a != null) {
                tfPatientId.setText(String.valueOf(a.getPatientId()));
                tfDoctorId.setText(String.valueOf(a.getDoctorId()));
                tfRoomId.setText(String.valueOf(a.getRoomId()));
                dpAppointmentDate.setValue(LocalDate.parse(a.getDate()));
                cbStatus.setValue(a.getStatus());
                tfBillingAmount.setText(String.valueOf(a.getBillingAmount()));
            }
        });
    }

    @FXML
    private void loadAppointments() {
        appointments.clear();
        try {
            ResultSet rs = DBUtils.executeQuery("SELECT * FROM appointments");
            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("room_id"),
                        rs.getDate("appointment_date").toString(),
                        rs.getString("status"),
                        rs.getDouble("billing_amount"),
                        rs.getDate("status_update_date").toString(),
                        rs.getString("secretary_username")
                ));
            }
            appointmentTable.setItems(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, room_id, appointment_date, status, billing_amount, status_update_date, secretary_username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(tfPatientId.getText()));
            stmt.setInt(2, Integer.parseInt(tfDoctorId.getText()));
            stmt.setInt(3, Integer.parseInt(tfRoomId.getText()));
            stmt.setDate(4, Date.valueOf(dpAppointmentDate.getValue()));
            stmt.setString(5, cbStatus.getValue());
            stmt.setDouble(6, Double.parseDouble(tfBillingAmount.getText()));
            stmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            stmt.setString(8, SessionManager.getLoggedInUser());

            stmt.executeUpdate();

            loadAppointments();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, room_id=?, appointment_date=?, status=?, billing_amount=? WHERE appointment_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(tfPatientId.getText()));
            stmt.setInt(2, Integer.parseInt(tfDoctorId.getText()));
            stmt.setInt(3, Integer.parseInt(tfRoomId.getText()));
            stmt.setDate(4, Date.valueOf(dpAppointmentDate.getValue()));
            stmt.setString(5, cbStatus.getValue());
            stmt.setDouble(6, Double.parseDouble(tfBillingAmount.getText()));
            stmt.setInt(7, selected.getId());
            stmt.executeUpdate();
            loadAppointments();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "DELETE FROM appointments WHERE appointment_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadAppointments();
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
        tfPatientId.clear();
        tfDoctorId.clear();
        tfRoomId.clear();
        dpAppointmentDate.setValue(null);
        cbStatus.setValue(null);
        tfBillingAmount.clear();
    }

    public static class Appointment {
        private final int id;
        private final int patientId;
        private final int doctorId;
        private final int roomId;
        private final String date;
        private final String status;
        private final double billingAmount;
        private final String statusUpdateDate;
        private final String secretary;

        public Appointment(int id, int patientId, int doctorId, int roomId, String date, String status, double billingAmount, String statusUpdateDate, String secretary) {
            this.id = id;
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.roomId = roomId;
            this.date = date;
            this.status = status;
            this.billingAmount = billingAmount;
            this.statusUpdateDate = statusUpdateDate;
            this.secretary = secretary;
        }

        public int getId() { return id; }
        public int getPatientId() { return patientId; }
        public int getDoctorId() { return doctorId; }
        public int getRoomId() { return roomId; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
        public double getBillingAmount() { return billingAmount; }
        public String getStatusUpdateDate() { return statusUpdateDate; }
        public String getSecretary() { return secretary; }
    }
}
