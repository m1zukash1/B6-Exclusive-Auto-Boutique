package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.*;
import com.b6exclusiveautoboutique.utils.DatabaseManager;
import com.mysql.cj.log.Log;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OrderController implements Initializable {
    public TableView<Order> tableView;
    public TableColumn<Order, Integer> idColumn;
    public TableColumn<Order, String> orderStatusColumn;
    public TableColumn<Order, Integer> assignedManagerIdColumn;
    public TableColumn<Order, String> productColumn;
    public TableColumn<Order, LocalDate> orderDateColumn;
    public ComboBox<String> dateComboBox;
    public ComboBox<Order.OrderStatus> statusComboBox;
    public ComboBox productYearComboBox;
    public List<Order> orders = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        dateComboBox.setItems(FXCollections.observableArrayList("Today", "Last Week", "Last Month", "All"));
        dateComboBox.valueProperty().addListener((obs, oldVal, newVal) -> filterOrdersByDate(newVal));

        statusComboBox.setItems(FXCollections.observableArrayList(Order.OrderStatus.values()));
        statusComboBox.valueProperty().addListener((obs, oldVal, newVal) -> filterOrdersByStatus(newVal));

        productYearComboBox.setItems(FXCollections.observableArrayList(Product.Year.values()));
        productYearComboBox.valueProperty().addListener((obs, oldVal, newVal) -> filterOrdersByProductYear((Product.Year) newVal));

        updateTableViewFromDB();
    }

    private void filterOrdersByProductYear(Product.Year year) {
        if (year == null) {
            updateTableViewFromDB(); // Show all if no year is selected
        } else {
            List<Order> filteredOrders = orders.stream()
                    .filter(o -> o.getProduct().getYear() == year)
                    .collect(Collectors.toList());
            tableView.setItems(FXCollections.observableArrayList(filteredOrders));
        }
    }

    private void filterOrdersByDate(String dateRange) {
        LocalDate now = LocalDate.now();
        List<Order> filteredOrders = null;

        switch (dateRange) {
            case "Today":
                filteredOrders = orders.stream()
                        .filter(o -> o.getOrderDate().equals(now))
                        .collect(Collectors.toList());
                break;
            case "Last Week":
                filteredOrders = orders.stream()
                        .filter(o -> o.getOrderDate().isAfter(now.minus(1, ChronoUnit.WEEKS)) && o.getOrderDate().isBefore(now))
                        .collect(Collectors.toList());
                break;
            case "Last Month":
                filteredOrders = orders.stream()
                        .filter(o -> o.getOrderDate().isAfter(now.minus(1, ChronoUnit.MONTHS)) && o.getOrderDate().isBefore(now))
                        .collect(Collectors.toList());
                break;
            case "All":
                filteredOrders = orders;
                break;
            default:
                break;
        }

        tableView.setItems(FXCollections.observableArrayList(filteredOrders));
    }
    private void filterOrdersByStatus(Order.OrderStatus status) {
        if (status == null) {
            updateTableViewFromDB(); // Show all if no status is selected
        } else {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
            GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
            List<Order> filteredOrders = genericHibernate.getAllRecords(Order.class).stream()
                    .filter(o -> o.getOrderStatus().equals(status))
                    .collect(Collectors.toList());
            tableView.setItems(FXCollections.observableArrayList(filteredOrders));
        }
    }
    public void updateTableViewFromDB() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);

        if(LoginController.connectedUser.getClass() == Admin.class) {
            orders = genericHibernate.getAllRecords(Order.class);
        } else {
            orders = genericHibernate.getAllRecords(Order.class).stream()
                    .filter(o -> o.getCustomer().getId() == LoginController.connectedUser.getId())
                    .collect(Collectors.toList());
        }

        tableView.setItems(FXCollections.observableArrayList(orders));
    }
    public void onDeleteOrderMenuItemPressed(ActionEvent actionEvent) {
        Order selectedOrder = tableView.getSelectionModel().getSelectedItem();

        if(selectedOrder.getCustomer().getId() != LoginController.connectedUser.getId()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("You cannot delete an order which is not yours");
            alert.showAndWait();
            return;
        }

        System.out.println(selectedOrder.getCustomer().getOrders());

        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.openConnection();
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "DELETE FROM orders WHERE id = ?"
            );
            preparedStatement.setInt(1, selectedOrder.getId());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
            selectedOrder.getCustomer().getOrders().remove(selectedOrder);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.closeConnection();
        }
        updateTableViewFromDB();
    }

    public void onRefreshButtonPressed(ActionEvent actionEvent) {
        updateTableViewFromDB();
    }

    public void onChangeStatusMenuItemPressed(ActionEvent actionEvent) {
        Order selectedOrder = tableView.getSelectionModel().getSelectedItem();

        if(LoginController.connectedUser.getClass() != Admin.class) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You are not an admin to do so!");
            alert.showAndWait();
            return;
        }

        if (selectedOrder == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No Order Selected");
            alert.showAndWait();
            return;
        }

        Dialog<Order.OrderStatus> dialog = new Dialog<>();
        dialog.setTitle("Change Order Status");
        dialog.setHeaderText("Select the new status for the order:");

        // Set up a ComboBox for the OrderStatus options
        ComboBox<Order.OrderStatus> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().setAll(Order.OrderStatus.values());
        statusComboBox.setValue(selectedOrder.getOrderStatus());

        // Create a custom dialog layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Order Status:"), 0, 0);
        grid.add(statusComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Add buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Convert the result to a OrderStatus when the OK button is clicked
        dialog.setResultConverter(new Callback<ButtonType, Order.OrderStatus>() {
            @Override
            public Order.OrderStatus call(ButtonType b) {
                if (b == ButtonType.OK) {
                    return statusComboBox.getValue();
                }
                return null;
            }
        });

        // Show the dialog and capture the result
        Optional<Order.OrderStatus> result = dialog.showAndWait();

        // Process the result
        if (result.isPresent()) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
            GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
            selectedOrder.setOrderStatus(result.get());
            genericHibernate.update(selectedOrder);
            updateTableViewFromDB();
        }
    }

    public void onAssignManagerMenuItemPressed(ActionEvent actionEvent) {
    }
}
