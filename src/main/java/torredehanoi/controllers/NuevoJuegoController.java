package torredehanoi.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import torredehanoi.model.ConfiguracionGlobal;
import torredehanoi.model.Disco;
import torredehanoi.model.Poste;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class NuevoJuegoController implements Initializable {

    @FXML private ImageView imgPotenciador;
    @FXML private StackPane panePotenciador;
    private boolean esPotenciador = true;
    private Timeline movimientoTimeline;
    private Timeline estadoTimeline;
    private static final double VELOCIDAD_CAIDA = 0.5;

    @FXML private Button btn_torre1Derecha;
    @FXML private Button btn_torre1Izquierda;
    @FXML private Button btn_torre2Derecha;
    @FXML private Button btn_torre2Izquierda;
    @FXML private Button btn_torre3Derecha;
    @FXML private Button btn_torre3Izquierda;

    @FXML private Label lbl_movimientos;
    @FXML private Label lbl_time;
    @FXML private Button btn_menuPrincipal;

    @FXML private AnchorPane panePoste1;
    @FXML private AnchorPane panePoste2;
    @FXML private AnchorPane panePoste3;

    private ConfiguracionGlobal configuracionGlobal = ConfiguracionGlobal.getInstance();
    private Poste[] postes;
    private AnchorPane[] panesPostes;
    private int movimientos;
    private int tiempoRestante;
    private Timeline timeline;

    private static final double ALTURA_DISCO = 25.0;
    private static final double ANCHO_BASE_POSTE = 30.0;
    private static final double BASE_Y = 289.0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        int cantidadDiscos = configuracionGlobal.getCantidadDiscos();
        tiempoRestante = configuracionGlobal.getTiempoPartidaSegundos();
        movimientos = 0;

        lbl_movimientos.setText("000");
        lbl_time.setText(String.valueOf(tiempoRestante));

        panesPostes = new AnchorPane[]{panePoste1, panePoste2, panePoste3};

        inicializarPostesYDiscos(cantidadDiscos);

        // Inicio de Hilos
        iniciarTemporizador();
        iniciarMovimientoPotenciador();
        iniciarCambioEstado();
    }

    private void inicializarPostesYDiscos(int numDiscos) {
        postes = new Poste[3];
        postes[0] = new Poste(1); // Origen
        postes[1] = new Poste(2); // Auxiliar
        postes[2] = new Poste(3); // Destino

        for (int i = numDiscos; i >= 1; i--) {
            Disco disco = new Disco(i, ANCHO_BASE_POSTE, ALTURA_DISCO);
            postes[0].colocarDisco(disco);
            agregarDiscoAUIPoste(disco, panesPostes[0], postes[0].getDiscos().size());
        }
    }

    private void agregarDiscoAUIPoste(Disco disco, AnchorPane posteUI, int posicionPila) {

        double discoY = BASE_Y - (posicionPila * ALTURA_DISCO);
        double centerX = posteUI.getPrefWidth() / 2;

        disco.getRepresentacionGrafica().setLayoutX(centerX - (disco.getRepresentacionGrafica().getWidth() / 2));
        disco.getRepresentacionGrafica().setLayoutY(discoY);

        if (!posteUI.getChildren().contains(disco.getRepresentacionGrafica())) {
            posteUI.getChildren().add(disco.getRepresentacionGrafica());
        }
    }

    private void iniciarTemporizador() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    tiempoRestante--;
                    // Actualización de la UI en el hilo de JavaFX
                    Platform.runLater(() -> {
                        lbl_time.setText(String.format("%02d", tiempoRestante));
                    });

                    if (tiempoRestante <= 0) {//Fin hilo temporizador
                        timeline.stop();
                        mostrarFinDeJuego(false);
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void moverDerecha(ActionEvent event) {
        Button boton = (Button) event.getSource();
        int indiceOrigen = -1;
        int indiceDestino = -1;

        if (boton.getId().equals("btn_torre1Derecha")) {
            indiceOrigen = 0; // Poste 1
            indiceDestino = 1; // Poste 2
        } else if (boton.getId().equals("btn_torre2Derecha")) {
            indiceOrigen = 1; // Poste 2
            indiceDestino = 2; // Poste 3
        }
        else if (boton.getId().equals("btn_torre3Derecha")) { // Envolvente: 3 -> 1
            indiceOrigen = 2; // Poste 3
            indiceDestino = 0; // Poste 1
        }

        if (indiceOrigen != -1) {
            intentarMoverDisco(indiceOrigen, indiceDestino);
        }
    }

    @FXML
    void moverIzquierda(ActionEvent event) {
        Button boton = (Button) event.getSource();
        int indiceOrigen = -1;
        int indiceDestino = -1;

        if (boton.getId().equals("btn_torre2Izquierda")) {
            indiceOrigen = 1; // Poste 2
            indiceDestino = 0; // Poste 1
        } else if (boton.getId().equals("btn_torre3Izquierda")) {
            indiceOrigen = 2; // Poste 3
            indiceDestino = 1; // Poste 2
        }
        else if (boton.getId().equals("btn_torre1Izquierda")) { // Envolvente: 1 -> 3
            indiceOrigen = 0; // Poste 1
            indiceDestino = 2; // Poste 3
        }

        if (indiceOrigen != -1) {
            intentarMoverDisco(indiceOrigen, indiceDestino);
        }
    }

    private void intentarMoverDisco(int indiceOrigen, int indiceDestino) {
        Poste origen = postes[indiceOrigen];
        Poste destino = postes[indiceDestino];

        Disco discoAMover = origen.getDiscoSuperior();

        if (discoAMover != null && destino.colocarDisco(discoAMover)) {
            origen.retirarDisco();

            panesPostes[indiceOrigen].getChildren().remove(discoAMover.getRepresentacionGrafica());
            agregarDiscoAUIPoste(discoAMover, panesPostes[indiceDestino], destino.getDiscos().size());

            movimientos++;
            lbl_movimientos.setText(String.format("%03d", movimientos));

            if (esJuegoGanado()) {
                timeline.stop();
                mostrarFinDeJuego(true);
            }
        } else {
            mostrarAlerta("Movimiento Inválido", "No se puede colocar un disco más grande sobre uno más pequeño.");
        }
    }

    private boolean esJuegoGanado() {
        return postes[2].getDiscos().size() == configuracionGlobal.getCantidadDiscos();
    }

    private void iniciarMovimientoPotenciador() {
        movimientoTimeline = new Timeline(
                new KeyFrame(Duration.millis(30), e -> {
                    double nuevaY = panePotenciador.getLayoutY() + VELOCIDAD_CAIDA;

                    if (nuevaY > 500.0) {//limite pantalla
                        nuevaY = -panePotenciador.getHeight();
                    }
                    panePotenciador.setLayoutY(nuevaY);
                })
        );
        movimientoTimeline.setCycleCount(Timeline.INDEFINITE);
        movimientoTimeline.play();// inicio hilo
    }

    private void iniciarCambioEstado() {
        cambiarIcono(true); // Potenciador

        estadoTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    esPotenciador = !esPotenciador; // Cambia el estado cada segundo
                    cambiarIcono(esPotenciador);
                })
        );
        estadoTimeline.setCycleCount(Timeline.INDEFINITE);
        estadoTimeline.play();
    }

    private void cambiarIcono(boolean esPotenciador) {
        String imagePath;
        if (esPotenciador) {
            imagePath = "/torredehanoi/imagen/potenciador_verde.jpg";
        } else {
            imagePath = "/torredehanoi/imagen/potenciador_rojo.jpg";
        }

        try {
            Image nuevaImagen = new Image(getClass().getResourceAsStream(imagePath));
            imgPotenciador.setImage(nuevaImagen);
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + imagePath + ". Asegúrate de que el archivo exista en la ruta de recursos.");
        }
    }

    @FXML
    void aplicarPotenciadorDebilitador(MouseEvent event) {
        if (timeline == null || timeline.getStatus() != Timeline.Status.RUNNING) {
            return;
        }

        if (esPotenciador) {
            tiempoRestante += 10;
        } else {
            tiempoRestante -= 10;
            if (tiempoRestante < 0) tiempoRestante = 0;
        }

        lbl_time.setText(String.valueOf(tiempoRestante));

        imgPotenciador.setDisable(true);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> imgPotenciador.setDisable(false));
                    }
                },
                2000 // 2 segundos de cooldown
        );
    }

    private void mostrarFinDeJuego(boolean ganado) {
        Alert alert;
        String mensaje;

        if (ganado) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Victoria!");
            alert.setHeaderText("¡Juego Terminado!");
            mensaje = String.format("Felicidades. Has ganado en %d movimientos con %d segundos restantes.",
                    movimientos, tiempoRestante);
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fin del Juego");
            alert.setHeaderText("Tiempo Agotado");
            mensaje = "No lograste completar la torre a tiempo. Inténtalo de nuevo.";
        }

        alert.setContentText(mensaje);
        alert.showAndWait();
        menuPrincipal(null);
    }

    private void navegar(ActionEvent event, String fxmlPath, String title) {

        if (timeline != null) timeline.stop();
        if (movimientoTimeline != null) movimientoTimeline.stop();
        if (estadoTimeline != null) estadoTimeline.stop();

        try {
            Node source = (event != null) ? (Node) event.getSource() : lbl_time;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al cargar la vista: " + fxmlPath).showAndWait();
        }
    }

    @FXML
    void menuPrincipal(ActionEvent event) {
        navegar(event, "/torredehanoi/application/pantallaPrincipal.fxml", "Torres de Hanoi");
    }

    @FXML
    void logout(ActionEvent event) {
        if (timeline != null) timeline.stop();
        if (movimientoTimeline != null) movimientoTimeline.stop();
        if (estadoTimeline != null) estadoTimeline.stop();
        Platform.exit();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Juego");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}