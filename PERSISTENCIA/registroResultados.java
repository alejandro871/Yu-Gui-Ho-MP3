package PERSISTENCIA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class registroResultados {

    public static void registrarResultado(String jugador1, String jugador2, String ganador) {

        try {

            BufferedWriter writer = new BufferedWriter( new FileWriter("resultados.txt", true));

            writer.write( LocalDateTime.now() + "|" + jugador1 + "|" + jugador2 + "|" + ganador);
            writer.newLine();
            writer.close();

            System.out.println("Resultado registrado.");

        } catch (IOException e) {

            System.out.println("Error al registrar resultado.");
            e.printStackTrace();
        }
    }
}