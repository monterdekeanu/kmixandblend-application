module com.unit.kmixandblendapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.unit.kmixandblendapplication to javafx.fxml;
    exports com.unit.kmixandblendapplication;
}