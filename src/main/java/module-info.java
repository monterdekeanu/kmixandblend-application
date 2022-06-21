module com.unit.kmixandblendapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires transitive java.desktop;
    opens com.unit.kmixandblendapplication to javafx.fxml;
    exports com.unit.kmixandblendapplication;
}