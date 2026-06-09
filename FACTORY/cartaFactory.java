package FACTORY;

import cartas.Carta;
import jugadores.Mazo;

public class cartaFactory {

   public static Carta crearCarta(String nombre) {

    if (nombre == null || nombre.trim().isEmpty()) {

        return null;
    }

     Carta carta = Mazo.buscarCartaPorNombre(nombre);

    if (carta == null) {

        System.out.println("Factory: carta no encontrada -> " + nombre);

        }

    return carta;
    }
}