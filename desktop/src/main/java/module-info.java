module com.example.kontras {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires mysql.connector.j;
    requires jakarta.persistence;
    requires lombok;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;

    opens com.example.kursinis to javafx.fxml, org.hibernate.orm.core, jakarta.persistence;
    exports com.example.kursinis;
    opens com.example.kursinis.fxControllers to javafx.fxml;
    opens com.example.kursinis.model to org.hibernate.orm.core, jakarta.persistence;
    exports com.example.kursinis.model;
}