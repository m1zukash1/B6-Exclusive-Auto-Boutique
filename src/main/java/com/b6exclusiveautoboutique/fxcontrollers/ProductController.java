package com.b6exclusiveautoboutique.fxcontrollers;
import com.b6exclusiveautoboutique.hibernate.GenericHibernate;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.b6exclusiveautoboutique.model.Product;

public class ProductController implements Initializable {
    public ComboBox<Product.Year> yearComboBox;
    public TextField priceTextField;
    public TextField mileageTextField;
    public ComboBox<Product.TransmissionType> transmissionTypeComboBox;
    public ComboBox<Product.FuelType> fuelTypeComboBox;
    public ComboBox<Product.EngineType> engineTypeComboBox;
    public ComboBox<Product.ExteriorColor> exteriorColorComboBox;
    public ComboBox<Product.InteriorType> interiorTypeComboBox;
    public TextArea descriptionTextArea;
    public ListView<Product> productListView;

    private ObservableList<Product> productsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yearComboBox.setItems(FXCollections.observableArrayList(Product.Year.values()));
        transmissionTypeComboBox.setItems(FXCollections.observableArrayList(Product.TransmissionType.values()));
        fuelTypeComboBox.setItems(FXCollections.observableArrayList(Product.FuelType.values()));
        engineTypeComboBox.setItems(FXCollections.observableArrayList(Product.EngineType.values()));
        exteriorColorComboBox.setItems(FXCollections.observableArrayList(Product.ExteriorColor.values()));
        interiorTypeComboBox.setItems(FXCollections.observableArrayList(Product.InteriorType.values()));
        engineTypeComboBox.setDisable(true);

        fuelTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            engineTypeComboBox.setDisable(newVal == null);

            if (newVal != null) {
                engineTypeComboBox.setItems(FXCollections.observableArrayList(
                        Stream.of(Product.EngineType.values())
                                .filter(engineType -> engineType.fuelType == newVal)
                                .collect(Collectors.toList())));
            } else {
                engineTypeComboBox.setItems(null);
            }
        });

        productListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateUIControls(newValue);
            }
        });
        updateProductListView();
    }
    private void updateProductListView() {
        productsList.clear();
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        productListView.setItems(FXCollections.observableArrayList(genericHibernate.getAllRecords(Product.class)));
    }
    private void updateUIControls(Product product) {
        yearComboBox.setValue(product.getYear());
        priceTextField.setText(String.valueOf(product.getPrice()));
        mileageTextField.setText(String.valueOf(product.getMileageKm()));
        transmissionTypeComboBox.setValue(product.getTransmissionType());
        fuelTypeComboBox.setValue(product.getFuelType());
        engineTypeComboBox.setValue(product.getEngineType());
        exteriorColorComboBox.setValue(product.getExteriorColor());
        interiorTypeComboBox.setValue(product.getInteriorType());
        descriptionTextArea.setText(product.getDescription());
    }

    public void onAddButtonAction(ActionEvent actionEvent) {
        try {
            if (validateInput()) return;

            float price = Float.parseFloat(priceTextField.getText());
            int mileage = Integer.parseInt(mileageTextField.getText());

            Product product = new Product(
                    yearComboBox.getValue(),
                    price,
                    mileage,
                    transmissionTypeComboBox.getValue(),
                    fuelTypeComboBox.getValue(),
                    engineTypeComboBox.getValue(),
                    exteriorColorComboBox.getValue(),
                    interiorTypeComboBox.getValue(),
                    descriptionTextArea.getText()
            );
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
            GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);

            genericHibernate.create(product);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please ensure price and mileage are valid numbers.");
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    public void onUpdateButtonAction(ActionEvent actionEvent) {

        if (validateInput()) return;

        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Update Error", "No product selected.");
            return;
        }

        try {
            float price = Float.parseFloat(priceTextField.getText());
            int mileage = Integer.parseInt(mileageTextField.getText());

            selectedProduct.setYear(yearComboBox.getValue());
            selectedProduct.setPrice(price);
            selectedProduct.setMileageKm(mileage);
            selectedProduct.setTransmissionType(transmissionTypeComboBox.getValue());
            selectedProduct.setFuelType(fuelTypeComboBox.getValue());
            selectedProduct.setEngineType(engineTypeComboBox.getValue());
            selectedProduct.setExteriorColor(exteriorColorComboBox.getValue());
            selectedProduct.setInteriorType(interiorTypeComboBox.getValue());
            selectedProduct.setDescription(descriptionTextArea.getText());

            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
            GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
            genericHibernate.update(selectedProduct);
            productListView.refresh();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please ensure price and mileage are valid numbers.");
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (yearComboBox.getValue() == null ||
                priceTextField.getText().trim().isEmpty() ||
                mileageTextField.getText().trim().isEmpty() ||
                transmissionTypeComboBox.getValue() == null ||
                fuelTypeComboBox.getValue() == null ||
                engineTypeComboBox.getValue() == null ||
                exteriorColorComboBox.getValue() == null ||
                interiorTypeComboBox.getValue() == null ||
                descriptionTextArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "All fields must be filled out.");
            return true;
        }
        return false;
    }


    public void onRemoveButtonAction(ActionEvent actionEvent) throws SQLException {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Remove Error", "No product selected.");
            return;
        }
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("b6_exclusive_auto_boutique");
        GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);
        genericHibernate.delete(Product.class, selectedProduct.getId());
        updateProductListView();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void clearUIControls() {
        yearComboBox.setValue(null);
        priceTextField.clear();
        mileageTextField.clear();
        transmissionTypeComboBox.setValue(null);
        fuelTypeComboBox.setValue(null);
        engineTypeComboBox.setValue(null);
        exteriorColorComboBox.setValue(null);
        interiorTypeComboBox.setValue(null);
        descriptionTextArea.clear();
    }
}
