package com.b6exclusiveautoboutique.fxcontrollers.usercontrollers;

import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.*;
import com.b6exclusiveautoboutique.utils.PasswordManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserWindowController implements Initializable {
    public TextField emailTextField;
    public TextField passwordTextField;
    public TextField nameTextField;
    public TextField surnameTextField;
    public ComboBox<String> accountTypeComboBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountTypeComboBox.getItems().addAll("Customer", "Manager", "Admin");
    }

    public void onCreateButtonPressed(ActionEvent actionEvent) {

        if (nameTextField.getText().trim().isEmpty() ||
                surnameTextField.getText().trim().isEmpty() ||
                emailTextField.getText().trim().isEmpty() ||
                passwordTextField.getText().trim().isEmpty() ||
                accountTypeComboBox.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return; // Return early if validation fails
        }

        String selectedAccountType = accountTypeComboBox.getSelectionModel().getSelectedItem();
        User newUser = null;

        switch (selectedAccountType) {
            case "Customer":
                newUser = new Customer();
                break;
            case "Manager":
                newUser = new Manager();
                break;
            case "Admin":
                newUser = new Admin();
                break;
        }

        if (newUser != null) {
            newUser.setName(nameTextField.getText());
            newUser.setSurname(surnameTextField.getText());
            newUser.setEmail(emailTextField.getText());
            newUser.setPassword(PasswordManager.generatePBKDF2WithHmacSHA1Password(passwordTextField.getText()));
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
            GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
            genericHibernate.create(newUser);
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }


}
