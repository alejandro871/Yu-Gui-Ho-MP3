package PERSISTENCIA;

import java.time.LocalDateTime;
import java.util.Stack;

public class historialGuardados {

    private static Stack<String> historial = new Stack<>();

    public static void registrarGuardado() {

        historial.push(
                "Guardado realizado: "
                        + LocalDateTime.now()
        );
    }

    public static void mostrarHistorial() {

        if (historial.isEmpty()) {

            System.out.println();
            System.out.println("No hay guardados registrados.");
            return;
        }

        System.out.println();
        System.out.println("===== HISTORIAL DE GUARDADOS =====");

        for (int i = historial.size() - 1; i >= 0; i--) {

            System.out.println(
                    historial.get(i)
            );
        }

        System.out.println("==================================");
    }
}