package PATRONES;

public class mementoPartida {

    private final String contenido;

    public mementoPartida(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    @Override
    public String toString() {
        return contenido;
    }
}