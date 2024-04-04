module com.kursinis.kursinisdarbas {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    requires org.kordamp.bootstrapfx.core;
    requires atlantafx.base;


    opens com.b6exclusiveautoboutique to javafx.fxml;
    exports com.b6exclusiveautoboutique;
    exports com.b6exclusiveautoboutique.fxcontrollers;
    opens com.b6exclusiveautoboutique.fxcontrollers to javafx.fxml;
}