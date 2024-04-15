package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.hibernate.grammars.hql.HqlParser;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ShopController implements Initializable {

    @FXML
    public ListView<Product> productListView;
    @FXML
    private TreeView<Comment> commentTreeView;

    private List<Product> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateProductListView();
    }

    private void updateProductListView() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            // JPQL query to find products not associated with any orders
            String jpql = "SELECT p FROM Product p WHERE p.id NOT IN (SELECT o.product.id FROM Order o)";
            List<Product> unorderedProducts = entityManager.createQuery(jpql, Product.class).getResultList();

            productList.clear();
            productList.addAll(unorderedProducts);
            productListView.setItems(FXCollections.observableArrayList(productList));
        } finally {
            entityManager.close();
        }
    }

    public void onAddToCardButtonPressed(ActionEvent actionEvent) {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        Customer currentlyConnectedCustomer = (Customer) LoginController.connectedUser;
        currentlyConnectedCustomer.getCart().getProductList().add(selectedProduct);
    }

    public void onAddCommentMenuItemPressed(ActionEvent actionEvent) {
    }

    public void onBuyButtonPressed(ActionEvent actionEvent) {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        Customer currentlyConnectedCustomer = (Customer) LoginController.connectedUser;

        Order order = new Order();
        order.setProduct(selectedProduct);
        order.setOrderDate(LocalDate.now());

        currentlyConnectedCustomer.getOrders().add(order);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);

        genericHibernate.create(order);
        genericHibernate.update(currentlyConnectedCustomer);

        updateProductListView();
    }
}
