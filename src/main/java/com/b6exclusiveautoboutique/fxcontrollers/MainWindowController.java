package com.b6exclusiveautoboutique.fxcontrollers;

import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController{
    public AnchorPane shopController;
    public TabPane tabPane;

    public void initialize() {

    }

    public void setCustomerView() {
        tabPane.getTabs().removeIf(tab -> !tab.getText().equals("Shop") && !tab.getText().equals("Orders"));
    }

    public void setAdminView() {
        return;
    }
}
