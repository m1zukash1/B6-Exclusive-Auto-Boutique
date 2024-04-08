package com.b6exclusiveautoboutique;

import com.b6exclusiveautoboutique.utils.PasswordManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        System.out.println(PasswordManager.generatePBKDF2WithHmacSHA1Password("customer"));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("B6 Exclusive Auto Boutique");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}