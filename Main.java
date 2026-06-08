import juego.Juego;
import CONTROLADOR.controladorJuego;
import jugadores.Jugador;
import jugadores.Mazo;
import VISTA.vistaConsola;
import VISTA.PantallaInicio;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║           YU-GI-OH! SIMULADOR             ║");
        System.out.println("║                    MP3                    ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║  Selecciona el modo de juego:             ║");
        System.out.println("║                                           ║");
        System.out.println("║   [1]  Modo Consola  (texto)              ║");
        System.out.println("║   [2]  Modo Gráfico  (GUI)                ║");
        System.out.println("║                                           ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.print("  Opcion: ");

        int modo = -1;
        try {
            modo = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
      
        }

        if (modo == 1) {
            iniciarModoConsola(scanner);
        } else if (modo == 2) {
            iniciarModoGrafico();
        } else {
            System.out.println("Opcion no valida. Iniciando modo gráfico por defecto...");
            iniciarModoGrafico();
        }
    }

    private static void iniciarModoConsola(Scanner scanner) {
        vistaConsola vista = new vistaConsola();

        System.out.println("\n═══ MODO CONSOLA ═══");
        System.out.print("  Nombre del Duelista 1: ");
        String nombre1 = scanner.nextLine().trim();
        if (nombre1.isEmpty()) nombre1 = "Alejo";

        System.out.print("  Nombre del Duelista 2: ");
        String nombre2 = scanner.nextLine().trim();
        if (nombre2.isEmpty()) nombre2 = "Juan";

        Jugador jugador1 = new Jugador(nombre1);
        Jugador jugador2 = new Jugador(nombre2);
        Mazo.repartir(jugador1, jugador2);

        Juego juego = new Juego(jugador1, jugador2);
        

        controladorJuego controlador = new controladorJuego(juego, vista);

        controlador.iniciarBucleConsola();
    }

    private static void iniciarModoGrafico() {
        System.out.println("\n  Iniciando interfaz grafica...");
        // Toda operación Swing debe ejecutarse en el EDT
        SwingUtilities.invokeLater(() -> {
            PantallaInicio pantalla = new PantallaInicio();
            pantalla.setVisible(true);
        });
    }

}
