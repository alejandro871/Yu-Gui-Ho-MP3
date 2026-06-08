package PERSISTENCIA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import cartas.Carta;
import jugadores.Mazo;

public class cargadorPartida {

    public static void mostrarArchivo() {

        try {

            BufferedReader reader = new BufferedReader( new FileReader("partida.txt"));

            String linea;

            System.out.println();
            System.out.println("===== PARTIDA GUARDADA =====");

            while ((linea = reader.readLine()) != null) {

                System.out.println(linea);
            }

            System.out.println("============================");

            reader.close();

        } catch (IOException e) {

            System.out.println("No se pudo leer partida.txt");
            e.printStackTrace();
        }
    }

    private static java.util.ArrayList<Carta> convertirCartas(String linea) {

    java.util.ArrayList<Carta> cartas = new java.util.ArrayList<>();

    if (linea == null || linea.isEmpty()) {
        return cartas;
    }

    String[] nombres = linea.split("\\|");

    for (String nombre : nombres) {

        Carta carta = Mazo.buscarCartaPorNombre(nombre.trim());

        if (carta != null) {
            cartas.add(carta);
        }
    }

    return cartas;
    }

    public static void cargarManoJugador1() {

    try {

        BufferedReader reader = new BufferedReader( new FileReader("partida.txt"));

        String linea;

        while ((linea = reader.readLine()) != null) {

            if (linea.startsWith("mano=")) {

                String datos = linea.substring(5);

                System.out.println();
                System.out.println("Cartas encontradas:");

                String[] nombres = datos.split("\\|");

                for (String nombre : nombres) {

                    Carta carta = Mazo.buscarCartaPorNombre(nombre.trim());

                    if (carta != null) { 
                        System.out.println(carta.getNombre() );
                    }
                }

                break;
            }
        }

        reader.close();

    } catch (IOException e) {

        e.printStackTrace();
    }
}

}