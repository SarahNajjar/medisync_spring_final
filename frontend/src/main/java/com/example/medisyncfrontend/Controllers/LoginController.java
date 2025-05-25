package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.DBUtils;
import com.example.medisyncfrontend.Utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = tfUsername.getText();
        String password = pfPassword.getText();

        String sql = "SELECT * FROM secretaries WHERE username=? AND password=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                SessionManager.setLoggedInUser(username);
                DBUtils.changeScene(event, "/com/example/medisyncfrontend/dashboard.fxml", "Welcome", username);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setContentText("Invalid username or password");
                alert.show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
