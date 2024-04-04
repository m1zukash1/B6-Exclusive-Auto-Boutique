module com.kursinis.kursinisdarbas {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    requires org.kordamp.bootstrapfx.core;
    requires atlantafx.base;


    opens com.kursinis.kursinisdarbas to javafx.fxml;
    exports com.kursinis.kursinisdarbas;
    exports com.kursinis.kursinisdarbas.fxcontrollers;
    opens com.kursinis.kursinisdarbas.fxcontrollers to javafx.fxml;
}