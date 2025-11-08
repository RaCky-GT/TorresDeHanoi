package torredehanoi.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PantallaPrincipalController {

    @FXML
    private Button btn_salir;

    @FXML
    private AnchorPane pnl_pantallaPrincipal;

    @FXML
    void nuevoJuego(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torredehanoi/application/nuevoJuego.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Torres de Hanoi - RaCky");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista del Nuevo Juego.");
        }
    }

    @FXML
    void juegoAutomatico(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torredehanoi/application/juegoAutomatico.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Torres de Hanoi - RaCky");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista del Nuevo Juego.");
        }

    }

    @FXML
    void top5(ActionEvent event) {

    }

    @FXML
    void configuracion(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torredehanoi/application/configuraciones.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Torres de Hanoi - RaCky");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista del Nuevo Juego.");
        }

    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        Platform.exit();
    }
}
