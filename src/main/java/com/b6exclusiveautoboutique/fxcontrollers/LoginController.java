package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.Main;
import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.User;
import com.b6exclusiveautoboutique.utils.DatabaseManager;
import com.b6exclusiveautoboutique.utils.PasswordManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField surnameTextField;

    public static User connectedUser = null;

    @FXML
    void onLoginButtonPressed(ActionEvent event) {
        String email = emailTextField.getText();
        String enteredPassword = passwordTextField.getText();

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        EntityManager entityManager = genericHibernate.getEntityManager();

        try {
            entityManager.getTransaction().begin();

            // Query to find the user by email
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();

            if (!users.isEmpty()) {
                User user = users.get(0);
                String hashedPassword = user.getPassword();
                String userType = user.getClass().getSimpleName(); // This assumes you have different classes for each user type

                // Check if the entered password matches the hashed password
                if (PasswordManager.validatePassword(enteredPassword, hashedPassword)) {
                    System.out.println("Login Successful. User Type: " + userType);

                    // Set the static connectedUser property
                    connectedUser = user;

                    // UI handling based on user role, using the class name to determine user type
                    switch (userType) {
                        case "Admin":
                            loginAsAdmin(event);
                            break;
                        case "Customer":
                            loginAsAdmin(event);
                        case "Manager":
                            // Add navigation logic for other user types if necessary
                            break;
                        default:
                            // Handle unknown user type if necessary
                            break;
                    }

                } else {
                    System.out.println("Incorrect Password. Login Failed.");
                }
            } else {
                System.out.println("Email not found. Login Failed.");
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
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
