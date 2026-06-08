package PERSISTENCIA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

public class rankingVictorias {

    public static void mostrarRanking() {

        HashMap<String, Integer> victorias = new HashMap<>();

        try {

            BufferedReader reader = new BufferedReader(new FileReader("resultados.txt"));

            String linea;

            while ((linea = reader.readLine()) != null) {

                String[] datos = linea.split("\\|");

                if (datos.length < 4) {
                    continue;
                }

                String ganador = datos[3];

                victorias.put( ganador, victorias.getOrDefault(ganador, 0) + 1);
             }

            reader.close();

        } catch (IOException e) {

            System.out.println("No se pudo leer resultados.txt");
            return;
        }

        TreeMap<String, Integer> ranking = new TreeMap<>(victorias);

        System.out.println();
        System.out.println("===== RANKING =====");

        for (String jugador : ranking.keySet()) {

            System.out.println( jugador + " -> " + ranking.get(jugador) + " victorias" );
        }

        System.out.println("===================");
    }
}