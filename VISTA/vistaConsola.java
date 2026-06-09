package VISTA;

import cartas.Carta;
import cartas.CartaTrampa;
import cartas.Monstruo;
import jugadores.Jugador;
import juego.Juego;
import PERSISTENCIA.registroResultados;
import java.util.List;
import java.util.Scanner;

public class vistaConsola implements vistaJuego {

    private final Scanner scanner;
    private boolean ganadorMostrado = false;

    public vistaConsola() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void actualizarEstado(Juego juego) {
        Jugador j1 = juego.getJugador1();
        Jugador j2 = juego.getJugador2();
        Jugador actual = juego.getJugadorActual();

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.printf("║  %-20s │ LP: %-5d │ Mazo: %-2d │ Mano: %d%n",
                j1.getNombre(), j1.getVida(), j1.getCartasMazo(), j1.getMano().size());
        mostrarCampoConsola(j1);
        System.out.println("║  ────────────────────────────────────────────────── ║");
        System.out.printf("║  %-20s │ LP: %-5d │ Mazo: %-2d │ Mano: %d%n",
                j2.getNombre(), j2.getVida(), j2.getCartasMazo(), j2.getMano().size());
        mostrarCampoConsola(j2);
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("   Turno de: " + actual.getNombre());
        System.out.println();
    }

    private void mostrarCampoConsola(Jugador j) {
        if (j.getCampo().isEmpty()) {
            System.out.println("║  Campo: (vacío)");
        } else {
            for (int i = 0; i < j.getCampo().size(); i++) {
                Monstruo m = j.getCampo().get(i);
                String pos   = m.isEnPosicionAtaque() ? "ATQ" : "DEF";
                String ataco = m.puedeAtacar() ? "" : " [ya atacó]";
                System.out.printf("║    [%d] %s Lv.%d %s ATK:%d DEF:%d%s%n",
                        i + 1, m.getNombre(), m.getNivel(), pos,
                        m.getAtk(), m.getDef(), ataco);
            }
        }
        if (!j.getTrampas().isEmpty()) {
            System.out.println("║  Trampas boca abajo: " + j.getTrampas().size());
        }
    }

    @Override
    public int elegirOpcionMenu(String titulo, String[] opciones) {
        System.out.println("\n─── " + titulo + " ───");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println("  [" + (i + 1) + "] " + opciones[i]);
        }
        System.out.println("  [0] Cancelar");
        System.out.print("  Opción: ");

        int opcion = leerEntero();
        if (opcion <= 0 || opcion > opciones.length) return -1;
        return opcion - 1;  
    }

    @Override
    public Monstruo elegirMonstruo(List<Monstruo> lista, String titulo) {
        if (lista.isEmpty()) {
            System.out.println("(No hay monstruos disponibles)");
            return null;
        }

        System.out.println("\n─── " + titulo + " ───");
        for (int i = 0; i < lista.size(); i++) {
            Monstruo m = lista.get(i);
            String pos = m.isEnPosicionAtaque() ? "ATQ" : "DEF";
            System.out.printf("  [%d] %s  Lv.%d [%s]  ATK:%d  DEF:%d%n",
                    i + 1, m.getNombre(), m.getNivel(), pos, m.getAtk(), m.getDef());
        }
        System.out.println("  [0] Cancelar");
        System.out.print("  Opción: ");

        int opcion = leerEntero();
        if (opcion <= 0 || opcion > lista.size()) return null;
        return lista.get(opcion - 1);
    }

    @Override
    public CartaTrampa elegirTrampa(List<CartaTrampa> lista, String titulo) {
        if (lista.isEmpty()) return null;

        System.out.println("\n─── " + titulo + " ───");
        System.out.println("  [0] No activar ninguna trampa");
        for (int i = 0; i < lista.size(); i++) {
            CartaTrampa t = lista.get(i);
            System.out.printf("  [%d] %s — %s%n", i + 1, t.getNombre(), t.getDescripcion());
        }
        System.out.print("  Opción: ");

        int opcion = leerEntero();
        if (opcion <= 0 || opcion > lista.size()) return null;
        return lista.get(opcion - 1);
    }

    @Override
    public boolean confirmar(String titulo, String mensaje) {
        System.out.println("\n " + titulo);
        System.out.println("  " + mensaje);
        System.out.print("  (s = sí / n = no): ");

        String resp = scanner.nextLine().trim().toLowerCase();
        return resp.equals("s") || resp.equals("si") || resp.equals("sí");
    }

    @Override
    public void mostrarGanador(Juego juego) {

        if (ganadorMostrado) {
            return;
        }

        ganadorMostrado = true;
    
        String ganador = juego.getNombreGanador();
        Jugador jGan = juego.getGanador();
        Jugador jPerder = (jGan == juego.getJugador1()) ? juego.getJugador2() : juego.getJugador1();

        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║             FIN DEL DUELO            ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  ¡¡ " + ganador.toUpperCase() + " GANA EL DUELO !!  ");
        System.out.println("║");
        System.out.printf("║  %s  -> LP finales: %d%n", jGan.getNombre(), jGan.getVida());
        System.out.printf("║  %s  -> LP finales: %d%n", jPerder.getNombre(), jPerder.getVida());
        System.out.println("║");
        System.out.println("║  \"Confía en el corazón de las cartas, Buena partida.\"");
        System.out.println("╚════════════════════════════════════════╝");

        registroResultados.registrarResultado(

        juego.getJugador1().getNombre(),
        juego.getJugador2().getNombre(),
        juego.getNombreGanador(),
        juego.getTurnos(),
        juego.getJugador1().getVida(),
        juego.getJugador2().getVida()

        );
    }

    @Override
    public boolean ofrecerNuevoDuelo() {
        return confirmar("Nuevo Duelo", "¿Quieres iniciar un nuevo duelo?");
    }

    @Override
    public void mostrarInfo(String titulo, String contenido) {
        System.out.println("\n=== " + titulo + " ===");
        System.out.println(contenido);
    }

    public void mostrarManoJugador(Jugador jugador) {
        List<Carta> mano = jugador.getMano();
        System.out.println("\n─── Mano de " + jugador.getNombre()
                + " (" + mano.size() + " cartas) ───");
        if (mano.isEmpty()) {
            System.out.println("  (mano vacía)");
            return;
        }
        for (int i = 0; i < mano.size(); i++) {
            Carta c = mano.get(i);
            System.out.printf("  [%d] %s%n", i + 1, c);
        }
    }

    public void esperarEnter(String mensaje) {
        System.out.println("\n" + mensaje);
        System.out.print("  [Presiona ENTER para continuar] ");
        scanner.nextLine();
    }

    public int elegirCartaDeMano(Jugador jugador) {
        mostrarManoJugador(jugador);
        if (jugador.getMano().isEmpty()) return -1;
        System.out.println("  [0] Cancelar");
        System.out.print("  Opción: ");
        int op = leerEntero();
        if (op <= 0 || op > jugador.getMano().size()) return -1;
        return op - 1;
    }

    private int leerEntero() {
        try {
            String linea = scanner.nextLine().trim();
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
