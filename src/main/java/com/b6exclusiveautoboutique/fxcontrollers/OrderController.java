package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.Manager;
import com.b6exclusiveautoboutique.model.Order;
import com.b6exclusiveautoboutique.model.Product;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {
    public TableView<Order> tableView;
    public TableColumn<Order, Integer> idColumn;
    public TableColumn<Order, String> orderStatusColumn;
    public TableColumn<Order, Integer> assignedManagerIdColumn;
    public TableColumn<Order, String> productColumn;
    public TableColumn<Order, LocalDate> orderDateColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
        assignedManagerIdColumn.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            Manager manager = order.getAssignedManager();
            return new ReadOnlyObjectWrapper<>(manager != null ? manager.getId() : null);
        });
        productColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue().getProduct();
            return new ReadOnlyObjectWrapper<>(product).asString();
        });
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        List<Order> orderList = genericHibernate.getAllRecords(Order.class);
        tableView.setItems(FXCollections.observableArrayList(orderList));
    }

}
