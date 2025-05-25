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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class WithdrawalController implements Initializable {

    @FXML
    private TableView<Withdrawal> withdrawalTable;

    @FXML private TableColumn<Withdrawal, Integer> colId;
    @FXML private TableColumn<Withdrawal, Integer> colDoctorId;
    @FXML private TableColumn<Withdrawal, String> colDate;
    @FXML private TableColumn<Withdrawal, Double> colAmount;
    @FXML private TableColumn<Withdrawal, String> colSecretary;

    @FXML private TextField tfDoctorId, tfAmount;
    @FXML private ComboBox<String> cbSecretary;
    @FXML private DatePicker dpWithdrawalDate;
    @FXML private Button btnAdd, btnUpdate, btnDelete;

    private final ObservableList<Withdrawal> withdrawals = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colSecretary.setCellValueFactory(new PropertyValueFactory<>("secretaryUsername"));

        loadSecretaries();
        loadWithdrawals();

        withdrawalTable.setOnMouseClicked(e -> {
            Withdrawal w = withdrawalTable.getSelectionModel().getSelectedItem();
            if (w != null) {
                tfDoctorId.setText(String.valueOf(w.getDoctorId()));
                tfAmount.setText(String.valueOf(w.getAmount()));
                cbSecretary.setValue(w.getSecretaryUsername());
                dpWithdrawalDate.setValue(LocalDate.parse(w.getDate()));
            }
        });
    }

    @FXML
    private void loadWithdrawals() {
        withdrawals.clear();
        try {
            ResultSet rs = DBUtils.executeQuery("SELECT * FROM withdrawals");
            while (rs.next()) {
                withdrawals.add(new Withdrawal(
                        rs.getInt("withdrawal_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("withdrawal_date").toString(),
                        rs.getDouble("amount"),
                        rs.getString("secretary_username")
                ));
            }
            withdrawalTable.setItems(withdrawals);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String sql = "INSERT INTO withdrawals (doctor_id, withdrawal_date, amount, secretary_username) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(tfDoctorId.getText()));
            stmt.setDate(2, Date.valueOf(dpWithdrawalDate.getValue()));
            stmt.setDouble(3, Double.parseDouble(tfAmount.getText()));
            stmt.setString(4, cbSecretary.getValue());

            stmt.executeUpdate();
            loadWithdrawals();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Withdrawal selected = withdrawalTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "UPDATE withdrawals SET doctor_id=?, withdrawal_date=?, amount=?, secretary_username=? WHERE withdrawal_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(tfDoctorId.getText()));
            stmt.setDate(2, Date.valueOf(dpWithdrawalDate.getValue()));
            stmt.setDouble(3, Double.parseDouble(tfAmount.getText()));
            stmt.setString(4, cbSecretary.getValue());
            stmt.setInt(5, selected.getId());

            stmt.executeUpdate();
            loadWithdrawals();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Withdrawal selected = withdrawalTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "DELETE FROM withdrawals WHERE withdrawal_id=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadWithdrawals();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        DBUtils.changeScene(event, "/com/example/medisyncfrontend/dashboard.fxml", "Dashboard", null);
    }

    private void loadSecretaries() {
        try {
            ResultSet rs = DBUtils.executeQuery("SELECT username FROM secretaries");
            ObservableList<String> secretaryList = FXCollections.observableArrayList();
            while (rs.next()) {
                secretaryList.add(rs.getString("username"));
            }
            cbSecretary.setItems(secretaryList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfDoctorId.clear();
        tfAmount.clear();
        cbSecretary.setValue(null);
        dpWithdrawalDate.setValue(null);
    }

    public static class Withdrawal {
        private final int id;
        private final int doctorId;
        private final String date;
        private final double amount;
        private final String secretaryUsername;

        public Withdrawal(int id, int doctorId, String date, double amount, String secretaryUsername) {
            this.id = id;
            this.doctorId = doctorId;
            this.date = date;
            this.amount = amount;
            this.secretaryUsername = secretaryUsername;
        }

        public int getId() { return id; }
        public int getDoctorId() { return doctorId; }
        public String getDate() { return date; }
        public double getAmount() { return amount; }
        public String getSecretaryUsername() { return secretaryUsername; }
    }
}
