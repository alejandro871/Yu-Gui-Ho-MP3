package PATRONES;

import java.time.LocalDateTime;

public class mementoPartida {

    private final String contenido;
    private final LocalDateTime fechaCreacion;

    public mementoPartida(String contenido) {
        this.contenido = contenido;
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public String toString() {
        return "Memento creado: " + fechaCreacion;
    }
}