package com.kursinis.kursinisdarbas;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 720);
        stage.setTitle("B6 Exclusive Auto Boutique");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}