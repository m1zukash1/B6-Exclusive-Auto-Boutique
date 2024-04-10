module com.kursinis.kursinisdarbas {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires mysql.connector.j;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires org.kordamp.bootstrapfx.core;
    requires org.junit.jupiter.api;


    opens com.b6exclusiveautoboutique to javafx.fxml;
    exports com.b6exclusiveautoboutique;
    exports com.b6exclusiveautoboutique.fxcontrollers;
    opens com.b6exclusiveautoboutique.fxcontrollers to javafx.fxml;
    opens com.b6exclusiveautoboutique.model to org.hibernate.orm.core, javafx.base;
}