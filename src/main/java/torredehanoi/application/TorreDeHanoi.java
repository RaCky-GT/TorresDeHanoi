package torredehanoi.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TorreDeHanoi extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("pantallaPrincipal.fxml"));

        // Crear la escena con el contenido cargado
        Scene scene = new Scene(root);

        // Configurar ventana
        stage.setTitle("Torre de Hanoi");
        stage.setScene(scene);

        // Mostrar ventana
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        System.out.println("hola");
    }
}