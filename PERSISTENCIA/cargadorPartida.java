package PERSISTENCIA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
}