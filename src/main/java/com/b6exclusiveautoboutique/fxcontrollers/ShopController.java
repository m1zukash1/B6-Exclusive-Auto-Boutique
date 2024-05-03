package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.Main;
import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import com.b6exclusiveautoboutique.model.*;
import com.b6exclusiveautoboutique.utils.DatabaseManager;
import com.mysql.cj.log.Log;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.grammars.hql.HqlParser;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        productListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            displayCommentsForSelectedProduct();  // Update the TreeView whenever a new product is selected
        });
    }

    private void displayCommentsForSelectedProduct() {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        selectedProduct = genericHibernate.getEntityById(Product.class, selectedProduct.getId());

        if (selectedProduct != null) {
            TreeItem<Comment> root = new TreeItem<>();
            root.setExpanded(true);

            // Filter to add only root comments (comments with no parent)
            for (Comment comment : selectedProduct.getComments()) {
                if (comment.getParentComment() == null) {  // Check if the comment has no parent
                    TreeItem<Comment> commentItem = new TreeItem<>(comment);
                    addCommentTreeItems(commentItem, comment.getReplies());  // Recursively add replies if any
                    root.getChildren().add(commentItem);
                }
            }
            commentTreeView.setRoot(root);
            commentTreeView.setShowRoot(false);
        } else {
            commentTreeView.setRoot(null);  // Clear the TreeView if no product is selected
        }
    }


    private void addCommentTreeItems(TreeItem<Comment> parentItem, List<Comment> replies) {
        for (Comment reply : replies) {
            TreeItem<Comment> childItem = new TreeItem<>(reply);
            parentItem.getChildren().add(childItem);
            addCommentTreeItems(childItem, reply.getReplies());  // Recurse to add further nested replies
        }
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

    public void onAddCommentMenuItemPressed(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        TreeItem<Comment> selectedCommentItem = commentTreeView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Product Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product before adding a comment.");
            alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-view.fxml"));
            Parent root = fxmlLoader.load();
            CommentViewController commentViewController = fxmlLoader.getController();

            if (selectedCommentItem != null) {
                commentViewController.init(selectedProduct, selectedCommentItem.getValue());
            } else {
                commentViewController.init(selectedProduct, null);
            }

            Scene mainScene = new Scene(root, 340, 400);
            Stage mainStage = new Stage();
            mainStage.setTitle("Add Comment");
            mainStage.setScene(mainScene);
            mainStage.initModality(Modality.APPLICATION_MODAL);
            mainStage.showAndWait();
        }
    }



    public void onBuyButtonPressed(ActionEvent actionEvent) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);

        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        Customer currentlyConnectedCustomer = (Customer) LoginController.connectedUser;

        Order order = new Order();
        order.setProduct(selectedProduct);
        order.setOrderDate(LocalDate.now());
        order.setCustomer(currentlyConnectedCustomer);
        currentlyConnectedCustomer.getOrders().add(order);
        genericHibernate.update(currentlyConnectedCustomer);
        updateProductListView();
    }

    public void onRefreshButtonPressed(ActionEvent actionEvent) {
        updateProductListView();
    }

    public void onDeleteCommentMenuItemPressed(ActionEvent actionEvent) {
        TreeItem<Comment> selectedCommentItem = commentTreeView.getSelectionModel().getSelectedItem();

        if (selectedCommentItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Comment selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a comment before deleting it.");
            alert.showAndWait();
            return;
        }
        else if (selectedCommentItem.getValue().getOwner().getId() != LoginController.connectedUser.getId() && LoginController.connectedUser.getClass() != Admin.class){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("You are not creator of the comment");
            alert.setHeaderText(null);
            alert.setContentText("Please select a comment you created.");
            alert.showAndWait();
            return;
        }
        else if (!selectedCommentItem.getValue().getReplies().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You cannot delete a comment which has children");
            alert.showAndWait();
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager();

        try {
            databaseManager.openConnection();
            PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                    "DELETE FROM comment WHERE id = ?"
            );
            preparedStatement.setInt(1, selectedCommentItem.getValue().getId());
            databaseManager.sendPreparedStatementQuery(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            databaseManager.closeConnection();
        }

    }
}
