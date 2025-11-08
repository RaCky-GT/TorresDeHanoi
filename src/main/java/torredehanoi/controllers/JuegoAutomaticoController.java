package torredehanoi.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import torredehanoi.model.ConfiguracionGlobal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class JuegoAutomaticoController implements Initializable {

    @FXML private Button btn_iniciar;
    @FXML private Button btn_regresar;
    @FXML private Label lblDiscos;
    @FXML private Label lblMovimientos;
    @FXML private TextArea txtAreaMovimientos;

    private ConfiguracionGlobal config = ConfiguracionGlobal.getInstance();
    private int cantidadDiscos;
    private int movimientos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cantidadDiscos = config.getCantidadDiscos();
        movimientos = 0;

        lblDiscos.setText(String.valueOf(cantidadDiscos));
        lblMovimientos.setText("000");
        txtAreaMovimientos.setText("Listo para resolver las Torres de Hanoi con " + cantidadDiscos + " discos.");
    }

    @FXML
    void iniciarJuegoAutomatico(ActionEvent event) {
        btn_iniciar.setDisable(true);
        txtAreaMovimientos.setText("");
        movimientos = 0;

        new Thread(() -> {

            resolverHanoi(cantidadDiscos, 1, 3, 2);

            Platform.runLater(() -> {
                txtAreaMovimientos.appendText("\nSe finalizó el juego");
            });

        }).start();
    }

    private void resolverHanoi(int n, int origen, int destino, int auxiliar) {
        if (n == 0) {
            return;
        }

        resolverHanoi(n - 1, origen, auxiliar, destino);
        moverDisco(n, origen, destino);
        resolverHanoi(n - 1, auxiliar, destino, origen);
    }

    private void moverDisco(int n, int origen, int destino) {
        String movimientoTexto = String.format("Se movió el Disco #%d del poste #%d al poste #%d\n", n, origen, destino);

        Platform.runLater(() -> {
            txtAreaMovimientos.appendText(movimientoTexto);
            movimientos++;
            lblMovimientos.setText(String.format("%03d", movimientos));
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @FXML
    void menuPrincipal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/torredehanoi/application/pantallaPrincipal.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Torres de Hanoi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista del Menú Principal.");
        }
    }
}