package com.example.medisyncfrontend.Controllers;

import com.example.medisyncfrontend.Utils.ApiClient;
import com.example.medisyncfrontend.Utils.SceneSwitcher;
import com.example.medisyncfrontend.Utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField tfPassword;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = tfUsername.getText().trim();
        String password = tfPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in both fields.");
            return;
        }

        // Prepare request body
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        // Make POST request
        boolean success = ApiClient.postLogin("/api/secretaries/login", body);

        if (success) {
            SessionManager.setLoggedInUser(username);
            SceneSwitcher.switchTo("/fxml/dashboard.fxml", "Dashboard");
        } else {
            showAlert("Login Failed", "Incorrect username or password.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
