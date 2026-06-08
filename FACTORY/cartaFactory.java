package FACTORY;

import cartas.Carta;
import jugadores.Mazo;

public class cartaFactory {

    public static Carta crearCarta(String nombre) {

        return Mazo.buscarCartaPorNombre(nombre);

    }
}