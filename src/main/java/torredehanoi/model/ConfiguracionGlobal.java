package torredehanoi.model;

public class ConfiguracionGlobal {

    private static ConfiguracionGlobal instance;

    private int cantidadDiscos;
    private int tiempoPartidaSegundos;

    private ConfiguracionGlobal() {
        this.cantidadDiscos = 3;
        this.tiempoPartidaSegundos = 120;
    }

    public static ConfiguracionGlobal getInstance() {
        if (instance == null) {
            instance = new ConfiguracionGlobal();
        }
        return instance;
    }

    public int getCantidadDiscos() {
        return cantidadDiscos;
    }

    public void setCantidadDiscos(int cantidadDiscos) {
        this.cantidadDiscos = cantidadDiscos;
    }

    public int getTiempoPartidaSegundos() {
        return tiempoPartidaSegundos;
    }

    public void setTiempoPartidaSegundos(int tiempoPartidaSegundos) {
        this.tiempoPartidaSegundos = tiempoPartidaSegundos;
    }
}
