package VISTA;

import cartas.CartaTrampa;
import cartas.Monstruo;
import juego.Juego;

import java.util.List;

public interface vistaJuego {

    void mostrarMensaje(String mensaje);

    void actualizarEstado(Juego juego);

    int elegirOpcionMenu(String titulo, String[] opciones);

    Monstruo elegirMonstruo(List<Monstruo> lista, String titulo);

    CartaTrampa elegirTrampa(List<CartaTrampa> lista, String titulo);

    boolean confirmar(String titulo, String mensaje);

    void mostrarGanador(Juego juego);

    boolean ofrecerNuevoDuelo();

    void mostrarInfo(String titulo, String contenido);
}
