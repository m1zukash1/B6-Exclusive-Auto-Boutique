package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UserController {

    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User, Integer> idColumn;
    @FXML
    public TableColumn<User, String> emailColumn;
    @FXML
    public TableColumn<User, String> passwordColumn;
    @FXML
    public TableColumn<User, String> nameColumn;
    @FXML
    public TableColumn<User, String> surnameColumn;
    @FXML
    public TableColumn<User, String> creditCardColumn;
    @FXML
    public TableColumn<User, String> shippingAddressColumn;
    @FXML
    public TableColumn<User, String> billingAddressColumn;
    @FXML
    public TableColumn<User, String> ordersColumn; // Assuming this is a String, adjust according to actual type
    @FXML
    public TableColumn<User, String> commentHistoryColumn; // Assuming this is a String, adjust accordingly
    @FXML
    public TableColumn<User, String> assignedOrdersColumn; // Adjust type as needed
    @FXML
    public TableColumn<User, String> managersColumn; // Adjust type as needed
    @FXML
    public TableColumn<User, String> accountTypeColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        creditCardColumn.setCellValueFactory(new PropertyValueFactory<>("creditCard"));
        shippingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        billingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("billingAddress"));
        ordersColumn.setCellValueFactory(new PropertyValueFactory<>("orders")); // You might need a custom cell factory if this isn't a simple property
        commentHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("commentHistory")); // Ditto
        assignedOrdersColumn.setCellValueFactory(new PropertyValueFactory<>("assignedOrders")); // Ditto
        managersColumn.setCellValueFactory(new PropertyValueFactory<>("managers")); // Ditto
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        //updateTableViewFromDB();
    }

    public void updateTableViewFromDB() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        List<User> userList = genericHibernate.getAllRecords(User.class);
        ObservableList<User> userObservableList = FXCollections.observableArrayList(userList);
        tableView.setItems(userObservableList);
    }
}
