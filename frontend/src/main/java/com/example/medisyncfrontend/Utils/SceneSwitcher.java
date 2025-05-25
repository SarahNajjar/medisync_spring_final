package com.example.medisyncfrontend.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class SceneSwitcher {

    public static void switchTo(String fxml, String title) {
        try {
            URL url = SceneSwitcher.class.getResource(fxml);
            if (url == null) {
                throw new IllegalStateException("FXML not found: " + fxml);
            }

            Parent root = FXMLLoader.load(url);

            Stage stage = (Stage) Stage.getWindows()
                    .filtered(Window::isShowing)
                    .get(0);
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
