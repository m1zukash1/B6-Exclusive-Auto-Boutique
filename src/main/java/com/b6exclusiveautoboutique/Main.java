package com.b6exclusiveautoboutique;

import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.*;
import com.b6exclusiveautoboutique.utils.PasswordManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        stage.setTitle("B6 Exclusive Auto Boutique");
        stage.setScene(scene);
        stage.show();
        System.out.println( PasswordManager.generatePBKDF2WithHmacSHA1Password("customer"));
    }

    public static void main(String[] args) {
        launch();
    }
}