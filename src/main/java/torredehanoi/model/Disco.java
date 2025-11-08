package torredehanoi.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Disco {

    private final int tamano;
    private final Rectangle representacionGrafica;

    public Disco(int numDisco, double anchoBase, double alto) {

        this.tamano = numDisco;

        double anchoDisco = anchoBase + (tamano * 30);

        this.representacionGrafica = new Rectangle(anchoDisco, alto);


        this.representacionGrafica.setArcWidth(10); // Bordes redondeados
        this.representacionGrafica.setArcHeight(10);
        this.representacionGrafica.setStroke(Color.BLACK); // Borde negro

        Color color;
        switch (tamano % 5) {
            case 1: color = Color.GREEN; break;
            case 2: color = Color.YELLOW; break;
            case 3: color = Color.ORANGE; break;
            case 4: color = Color.RED; break;
            default: color = Color.BLUE; break;
        }
        this.representacionGrafica.setFill(color);
    }

    public int getTamano() {
        return tamano;
    }

    public Rectangle getRepresentacionGrafica() {
        return representacionGrafica;
    }
}