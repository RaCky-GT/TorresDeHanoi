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

        Parent root = FXMLLoader.load(getClass().getResource("pantallaPrincipal.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("Torre de Hanoi");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        System.out.println("hola");
    }
}