package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.Main;
import com.b6exclusiveautoboutique.utils.DatabaseManager;
import com.b6exclusiveautoboutique.utils.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    void onLoginButtonPressed(ActionEvent event) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        try {
            String email = emailTextField.getText();
            String enteredPassword = passwordTextField.getText();

            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "SELECT password, account_type FROM user WHERE email = ?"
            );
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                String accountType = resultSet.getString("account_type");

                if (PasswordManager.validatePassword(enteredPassword, hashedPassword)) {
                    System.out.println("Login Successful. Account Type: " + accountType);

                    switch (accountType) {
                        case "ADMIN":
                            loginAsAdmin(event);
                        case "CUSTOMER":
                        case "MANAGER":
                    }

                } else {
                    System.out.println("Incorrect Password. Login Failed.");
                }
            } else {
                System.out.println("Email not found. Login Failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }


    @FXML
    void onRegisterButtonPressed(ActionEvent event) {

    }

    void loginAsAdmin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 1600, 900);
        Stage mainStage = new Stage();
        mainStage.setTitle("Admin View");
        mainStage.setScene(mainScene);

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        mainStage.show();
    }

}
