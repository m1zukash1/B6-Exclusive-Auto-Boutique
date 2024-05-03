package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.Main;
import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.Customer;
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
import javafx.scene.control.Alert;
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

            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();

            if (!users.isEmpty()) {
                User user = users.get(0);
                String hashedPassword = user.getPassword();
                String userType = user.getClass().getSimpleName();

                if (PasswordManager.validatePassword(enteredPassword, hashedPassword)) {
                    System.out.println("Login Successful. User Type: " + userType);

                    connectedUser = user;

                    switch (userType) {
                        case "Admin":
                            loginAsAdmin(event);
                            break;
                        case "Customer":
                            loginAsCustomer(event);
                            break;
                        case "Manager":
                            loginAsManager(event);
                            break;
                        default:
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
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "All fields are required.");
            return;
        }

        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Enter a valid email address.");
            return;
        }

        String hashedPassword = PasswordManager.generatePBKDF2WithHmacSHA1Password(password);
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPassword(hashedPassword);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        EntityManager entityManager = genericHibernate.getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(customer);
            entityManager.getTransaction().commit();

            showAlert(Alert.AlertType.INFORMATION, "Registration Success", "Registration successful!");
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to register due to an error: " + e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
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

    void loginAsManager(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 1600, 900);
        Stage mainStage = new Stage();
        mainStage.setTitle("Manager View");
        mainStage.setScene(mainScene);

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        mainStage.show();
    }

    void loginAsCustomer(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 1600, 900);
        MainWindowController controller = fxmlLoader.getController();
        controller.setCustomerView();

        Stage mainStage = new Stage();
        mainStage.setTitle("Customer View");
        mainStage.setScene(mainScene);

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        mainStage.show();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
