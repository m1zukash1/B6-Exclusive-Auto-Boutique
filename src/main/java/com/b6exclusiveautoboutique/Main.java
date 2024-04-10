package com.b6exclusiveautoboutique;

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
        //Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        System.out.println(PasswordManager.generatePBKDF2WithHmacSHA1Password("customer"));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        stage.setTitle("B6 Exclusive Auto Boutique");
        stage.setScene(scene);
        stage.show();


        Customer testCustomer = new Customer();

// Set basic user information
        testCustomer.setName("John");
        testCustomer.setSurname("Doe");
        testCustomer.setEmail("johndoe@example.com");
        testCustomer.setPassword("password123");

// Set credit card information (optional)
        CreditCard testCreditCard = new CreditCard();
        testCreditCard.setCardNumber("1234567890123456");
        testCreditCard.setCardholderName("John Doe"); // Assuming both first and last name are stored
        testCreditCard.setExpirationDate(LocalDate.of(2025, 12, 31)); // Set expiration date
        testCreditCard.setCvc(123);
        testCreditCard.setCardType("Visa"); // Example card type
        testCustomer.setCreditCard(testCreditCard);

// Set address information (optional)
        Address testAddress = new Address();
        testAddress.setFirstName("John");
        testAddress.setLastName("Doe");
        testAddress.setStreetAddress("123 Main St");
        testAddress.setCity("Anytown");
        testAddress.setPostalCode("12345");
// You can set additional address fields like secondaryStreetAddress, tertiaryStreetAddress, and country if needed

// Assign the same address for both shipping and billing (optional)
        testCustomer.setShippingAddress(testAddress);
        testCustomer.setBillingAddress(testAddress);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);


    }

    public static void main(String[] args) {
        launch();
    }
}