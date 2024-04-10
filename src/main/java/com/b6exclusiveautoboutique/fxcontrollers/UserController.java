package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.Admin;
import com.b6exclusiveautoboutique.model.Customer;
import com.b6exclusiveautoboutique.model.Manager;
import com.b6exclusiveautoboutique.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    public TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> accountTypeColumn;

    @FXML
    private TableColumn<User, Integer> assignedOrdersColumn;

    @FXML
    private TableColumn<User, Integer> billingAddressColumn;

    @FXML
    private TableColumn<User, Integer> commentHistoryColumn;

    @FXML
    private TableColumn<User, Integer> creditCardColumn;

    @FXML
    private TableColumn<User, String > emailColumn;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, Integer> managersColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, Integer> shippingAddressColumn;

    @FXML
    private TableView<User> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        creditCardColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Customer) {
                Customer customer = (Customer) cellData.getValue();
                return customer.getCreditCard() != null
                        ? new SimpleIntegerProperty(customer.getCreditCard().getId()).asObject()
                        : new SimpleIntegerProperty().asObject(); // Assuming a CreditCard has an ID field
            } else {
                return new SimpleIntegerProperty().asObject();
            }
        });
        shippingAddressColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Customer) {
                Customer customer = (Customer) cellData.getValue();
                return customer.getShippingAddress() != null
                        ? new SimpleIntegerProperty(customer.getShippingAddress().getId()).asObject() // Corrected part
                        : new SimpleIntegerProperty().asObject();
            } else {
                return new SimpleIntegerProperty().asObject();
            }
        });
        billingAddressColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Customer) {
                Customer customer = (Customer) cellData.getValue();
                return customer.getBillingAddress() != null
                        ? new SimpleIntegerProperty(customer.getBillingAddress().getId()).asObject() // Corrected part
                        : new SimpleIntegerProperty().asObject();
            } else {
                return new SimpleIntegerProperty().asObject();
            }
        });
        assignedOrdersColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Manager) {
                Manager manager = (Manager) cellData.getValue();
                return new SimpleIntegerProperty(manager.getAssignedOrders().size()).asObject();
            } else {
                return new SimpleIntegerProperty().asObject();
            }
        });
        accountTypeColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            if (user instanceof Customer) {
                return new SimpleStringProperty("Customer");
            } else if (user instanceof Admin) {
                return new SimpleStringProperty("Admin");
            } else if (user instanceof Manager) {
                return new SimpleStringProperty("Manager");
            } else {
                return new SimpleStringProperty("User");
            }
        });
        updateTableViewFromDB();
    }

    public void updateTableViewFromDB() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        List<User> userList = genericHibernate.getAllRecords(User.class);
        ObservableList<User> userObservableList = FXCollections.observableArrayList(userList);
        tableView.setItems(userObservableList);
    }
}
