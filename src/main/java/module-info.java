module torredehanoi {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    exports torredehanoi.application;
    opens torredehanoi.application to javafx.graphics, javafx.fxml;
    opens torredehanoi.controllers to javafx.fxml;
}