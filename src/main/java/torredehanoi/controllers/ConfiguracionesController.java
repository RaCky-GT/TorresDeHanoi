package torredehanoi.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import torredehanoi.model.ConfiguracionGlobal;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfiguracionesController implements Initializable{

    @FXML private Button btn_guardar;
    @FXML private ChoiceBox<Integer> cbx_cantidadDiscos;
    @FXML private TextField txt_tiempoPartida;

    private ConfiguracionGlobal configuracionGlobal = ConfiguracionGlobal.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Integer> discos = FXCollections.observableArrayList(3, 4, 5, 6, 7);
        cbx_cantidadDiscos.setItems(discos);

        //Cargar valores iniciales desde ConfiguracionGlobal
        cbx_cantidadDiscos.getSelectionModel().select(Integer.valueOf(configuracionGlobal.getCantidadDiscos()));
        txt_tiempoPartida.setText(String.valueOf(configuracionGlobal.getTiempoPartidaSegundos()));
    }

    @FXML
    void guardarConfiguraciones(ActionEvent event) {
        if (validarYGuardar()) {
            navegar(event, "/torredehanoi/application/pantallaPrincipal.fxml", "Torres de Hanoi");
        }
    }

    @FXML
    void regresarAPrincipal(ActionEvent event) {
        navegar(event, "/torredehanoi/application/pantallaPrincipal.fxml", "Torres de Hanoi");
    }

    private boolean validarYGuardar() {
        Integer discosSeleccionados = cbx_cantidadDiscos.getSelectionModel().getSelectedItem();
        if (discosSeleccionados != null) {
            configuracionGlobal.setCantidadDiscos(discosSeleccionados);
        } else {
            mostrarAlerta("Error de Discos", "Debe seleccionar una cantidad de discos (3 a 7).");
            return false;
        }

        String tiempoTexto = txt_tiempoPartida.getText().trim();
        try {
            int tiempo = Integer.parseInt(tiempoTexto);

            if (tiempo > 0) {
                configuracionGlobal.setTiempoPartidaSegundos(tiempo);
                return true;
            } else {
                mostrarAlerta("Error de Tiempo", "El tiempo debe ser un número positivo (mayor que 0).");
                return false;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta("Error de Formato", "El tiempo de partida debe ser un número entero válido.");
            return false;
        }
    }

    private void navegar(ActionEvent event, String fxmlPath, String title) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista: " + fxmlPath);
            mostrarAlerta("Error de Navegación", "No se pudo cargar la vista. Revise la ruta del FXML.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Configuración");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
