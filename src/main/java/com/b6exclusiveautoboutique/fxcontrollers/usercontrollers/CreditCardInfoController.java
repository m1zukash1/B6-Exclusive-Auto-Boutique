package com.b6exclusiveautoboutique.fxcontrollers.usercontrollers;

import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.Customer;
import com.b6exclusiveautoboutique.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreditCardInfoController {

    @FXML
    private TextField cardHolderNameTextField;

    @FXML
    private TextField cardNumberTextField;

    @FXML
    private TextField cvcTextField;

    @FXML
    private DatePicker expirationDateDatePicker;

    Customer customer = null;
    void init(Customer user) {
        customer = user;
        cardHolderNameTextField.setText(user.getCreditCard().getCardholderName());
        cardNumberTextField.setText(user.getCreditCard().getCardNumber());
        cvcTextField.setText(String.valueOf(user.getCreditCard().getCvc()));
        expirationDateDatePicker.setValue(user.getCreditCard().getExpirationDate());
    }
    @FXML
    void onUpdateButtonPressed(ActionEvent event) {
        customer.getCreditCard().setCardholderName(cardHolderNameTextField.getText());
        customer.getCreditCard().setCardNumber(cardNumberTextField.getText());
        customer.getCreditCard().setCvc(Integer.parseInt(cvcTextField.getText()));
        customer.getCreditCard().setExpirationDate(expirationDateDatePicker.getValue());

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        genericHibernate.update(customer);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
