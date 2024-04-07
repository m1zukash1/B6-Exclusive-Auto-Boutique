package com.b6exclusiveautoboutique.fxcontrollers;
import com.b6exclusiveautoboutique.utils.DatabaseManager;
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
        productsList = fetchProductsFromDB();
        productListView.setItems(productsList);
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
            addProductToDB(product);
            updateProductListView();
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

            updateProductInDB(selectedProduct);
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
        removeProductFromDB(selectedProduct);
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

    private void addProductToDB(Product product) throws SQLException {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                "INSERT INTO product (year, price, mileage_km, transmission_type, fuel_type, engine_type, exterior_color, interior_type, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        preparedStatement.setString(1, product.getYear().name());
        preparedStatement.setFloat(2, product.getPrice());
        preparedStatement.setInt(3, product.getMileageKm());
        preparedStatement.setString(4, product.getTransmissionType().name());
        preparedStatement.setString(5, product.getFuelType().name());
        preparedStatement.setString(6, product.getEngineType().name());
        preparedStatement.setString(7, product.getExteriorColor().name());
        preparedStatement.setString(8, product.getInteriorType().name());
        preparedStatement.setString(9, product.getDescription());

        databaseManager.sendPreparedStatementQuery(preparedStatement);
        databaseManager.closeConnection();
    }
    private void removeProductFromDB(Product product) throws SQLException {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                "DELETE FROM product WHERE id = ?"
        );

        preparedStatement.setInt(1, product.getId());
        databaseManager.sendPreparedStatementQuery(preparedStatement);
        updateProductListView();
        databaseManager.closeConnection();
    }
    private void updateProductInDB(Product product) throws SQLException {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        PreparedStatement preparedStatement = databaseManager.getConnection().prepareStatement(
                "UPDATE product SET year = ?, price = ?, mileage_km = ?, transmission_type = ?, fuel_type = ?, engine_type = ?, exterior_color = ?, interior_type = ?, description = ? WHERE id = ?"
        );

        preparedStatement.setString(1, product.getYear().name());
        preparedStatement.setFloat(2, product.getPrice());
        preparedStatement.setInt(3, product.getMileageKm());
        preparedStatement.setString(4, product.getTransmissionType().name());
        preparedStatement.setString(5, product.getFuelType().name());
        preparedStatement.setString(6, product.getEngineType().name());
        preparedStatement.setString(7, product.getExteriorColor().name());
        preparedStatement.setString(8, product.getInteriorType().name());
        preparedStatement.setString(9, product.getDescription());
        preparedStatement.setInt(10, product.getId());

        databaseManager.sendPreparedStatementQuery(preparedStatement);
        databaseManager.closeConnection();
    }
    private ObservableList<Product> fetchProductsFromDB() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.openConnection();

        databaseManager.sendStatementQuery("SELECT * FROM product");
        ResultSet resultSet = databaseManager.getResultSet();

        try {
            while (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getInt("id"),
                            Product.Year.valueOf(resultSet.getString("year")),
                            resultSet.getFloat("price"),
                            resultSet.getInt("mileage_km"),
                            Product.TransmissionType.valueOf(resultSet.getString("transmission_type")),
                            Product.FuelType.valueOf(resultSet.getString("fuel_type")),
                            Product.EngineType.valueOf(resultSet.getString("engine_type")),
                            Product.ExteriorColor.valueOf(resultSet.getString("exterior_color")),
                            Product.InteriorType.valueOf(resultSet.getString("interior_type")),
                            resultSet.getString("description")
                    );
                productsList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
        return productsList;
    }

}
