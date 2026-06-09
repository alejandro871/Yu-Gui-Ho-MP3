package PERSISTENCIA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

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

        ArrayList<Map.Entry<String, Integer>> ranking = new ArrayList<>(victorias.entrySet());

        Collections.sort( ranking, Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed());

        System.out.println();
        System.out.println("===== RANKING =====");
        
        for (Map.Entry<String, Integer> entrada : ranking) {

            System.out.println( entrada.getKey() + " -> " + entrada.getValue() + " victorias");
        }

        System.out.println("===================");
    }
}