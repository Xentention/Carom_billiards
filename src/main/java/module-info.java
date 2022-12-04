module coursework.carom_billiards {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;

    opens coursework.carom_billiards to javafx.fxml;
    exports coursework.carom_billiards;
}