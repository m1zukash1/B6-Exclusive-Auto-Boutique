package com.b6exclusiveautoboutique.fxcontrollers;

import com.b6exclusiveautoboutique.model.Comment;
import com.b6exclusiveautoboutique.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CommentViewController {

    @FXML
    private TextArea commentBodyTextArea;

    @FXML
    private TextField headerTextField;

    Product product = null;
    Comment parentComment = null;

    void init(Product product, Comment parentComment) {
        this.product = product;
        this.parentComment = parentComment;  // Store the parent comment
    }
    @FXML
    void onCreateButtonPressed(ActionEvent event) {
        if (product == null) {
            showAlert("Error", "No product selected", "Please select a product to add comments to.", Alert.AlertType.ERROR);
            return;
        }

        String header = headerTextField.getText().trim();
        String body = commentBodyTextArea.getText().trim();

        if (header.isEmpty() || body.isEmpty()) {
            showAlert("Incomplete Fields", "Both fields are required", "Please enter both a title and a body for the comment.", Alert.AlertType.WARNING);
        } else {
            Comment newComment = new Comment();
            newComment.setTitle(header);
            newComment.setCommentBody(body);
            newComment.setProduct(product);
            newComment.setParentComment(parentComment);
            newComment.setOwner(LoginController.connectedUser);

            product.getComments().add(newComment);

            saveComment(newComment);

            headerTextField.setText("");
            commentBodyTextArea.setText("");
            showAlert("Comment Added", null, "Your comment has been successfully added.", Alert.AlertType.INFORMATION);
        }
    }

    private void saveComment(Comment comment) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(comment);
        em.getTransaction().commit();
        em.close();
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
