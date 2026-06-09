package CONTROLADOR;
import VISTA.vistaConsola;
import VISTA.vistaJuego;
import cartas.Carta;
import cartas.CartaMagica;
import cartas.CartaTrampa;
import cartas.Monstruo;
import efectos.Contexto;
import juego.Juego;
import jugadores.Jugador;


import java.util.ArrayList;
import java.util.List;

public class controladorJuego {

    private final Juego juego;
    private final vistaJuego vista;
    private boolean yaRoboEsteTurno;
    

    public controladorJuego(Juego juego, vistaJuego vista) {
        this.juego = juego;
        this.vista = vista;
        this.yaRoboEsteTurno = false;
    }

    public Juego   getJuego() { return juego; }
    public boolean isYaRoboEsteTurno() { return yaRoboEsteTurno; }

    public boolean accionRobar() {
        if (yaRoboEsteTurno) {
            vista.mostrarMensaje("  Ya robaste una carta este turno.");
            return true;
        }

        Jugador actual = juego.getJugadorActual();
        vista.mostrarMensaje("\n[ Fase de Robo — " + actual.getNombre() + " ]");

        boolean puedeRobar = juego.faseRobo();

        if (!puedeRobar) {
            vista.actualizarEstado(juego);
            return false;   // jugador eliminado por mazo vacío
        }

        List<Carta> mano = actual.getMano();
        if (!mano.isEmpty()) {
            Carta robada = mano.get(mano.size() - 1);
            vista.mostrarMensaje("  → Robaste: " + robada.getNombre()
                    + "  [" + robada.getTipo() + "]");
        }

        yaRoboEsteTurno = true;
        vista.actualizarEstado(juego);
        return true;
    }

    public void accionJugarCarta(Carta carta) {
        Jugador actual  = juego.getJugadorActual();
        Jugador enemigo = juego.getJugadorEnemigo();

        if (!yaRoboEsteTurno) {
            vista.mostrarMensaje("  Debes robar tu carta primero (Fase de Robo).");
            return;
        }
        if (actual.yaJugoCartaEsteTurno()) {
            vista.mostrarMensaje("  Ya jugaste una carta este turno. Solo se permite 1 por turno.");
            return;
        }

        if (carta instanceof Monstruo) {
            procesarInvocacion((Monstruo) carta, actual);
        } else if (carta instanceof CartaMagica) {
            procesarMagia((CartaMagica) carta, actual, enemigo);
        } else if (carta instanceof CartaTrampa) {
            procesarColocarTrampa((CartaTrampa) carta, actual);
        }

        vista.actualizarEstado(juego);
        verificarFin();
    }

    public void accionAtacar() {
        Jugador actual  = juego.getJugadorActual();
        Jugador enemigo = juego.getJugadorEnemigo();

        if (!yaRoboEsteTurno) {
            vista.mostrarMensaje("  Primero debes robar tu carta (Fase de Robo).");
            return;
        }
        if (juego.esPrimerTurno()) {
            vista.mostrarMensaje("  ¡En el primer turno no se puede atacar!");
            return;
        }
        if (actual.getCampo().isEmpty()) {
            vista.mostrarMensaje("  No tienes monstruos en campo para atacar.");
            return;
        }

        List<Monstruo> disponibles = new ArrayList<>();
        for (Monstruo m : actual.getCampo()) {
            if (m.puedeAtacar()) disponibles.add(m);
        }

        if (disponibles.isEmpty()) {
            vista.mostrarMensaje("  Todos tus monstruos ya atacaron este turno.");
            return;
        }

        // 1. Elegir atacante
        Monstruo atacante = vista.elegirMonstruo(disponibles, "Elige tu monstruo atacante");
        if (atacante == null) {
            vista.mostrarMensaje("  Ataque cancelado.");
            return;
        }

        vista.mostrarMensaje("\n[ Fase de Batalla ]");
        vista.mostrarMensaje("  " + actual.getNombre() + " ataca con: " + atacante.getNombre());

        Contexto ctxTrampa = procesarTrampasEnemigas(enemigo, actual, atacante);
        if (ctxTrampa != null && ctxTrampa.isAtaqueAnulado()) {
            vista.mostrarMensaje("  ¡El ataque fue CANCELADO por una trampa!");
            atacante.marcarComoAtacado();
            vista.actualizarEstado(juego);
            verificarFin();
            return;
        }

        // Verificar si el atacante sobrevivió a la trampa
        if (!actual.getCampo().contains(atacante)) {
            vista.mostrarMensaje("  El monstruo atacante fue destruido por una trampa.");
            vista.actualizarEstado(juego);
            verificarFin();
            return;
        }
        Monstruo defensor = null;
        if (!enemigo.getCampo().isEmpty()) {
            defensor = vista.elegirMonstruo(
                    new ArrayList<>(enemigo.getCampo()),
                    "Elige el monstruo a atacar (" + enemigo.getNombre() + ")");
            if (defensor == null) {
                vista.mostrarMensaje("  Ataque cancelado.");
                return;
            }
        } else {
            boolean confirmar = vista.confirmar("¡Ataque Directo!", enemigo.getNombre() + " no tiene monstruos. ¿Atacar directamente con " + atacante.getNombre() + " (ATK:" + atacante.getAtk() + ")?");
            if (!confirmar) {
                vista.mostrarMensaje("  Ataque directo cancelado.");
                return;
            }
            vista.mostrarMensaje("  ¡ATAQUE DIRECTO! -> "
                    + atacante.getAtk() + " daño a " + enemigo.getNombre());
        }

        int lpEnemigoAntes = enemigo.getVida();
        int lpPropioAntes  = actual.getVida();

        actual.atacarConMonstruo(atacante, enemigo, defensor);

        // Reportar diferencias de LP en el log de la vista
        if (enemigo.getVida() < lpEnemigoAntes) {
            vista.mostrarMensaje("  → " + enemigo.getNombre()
                    + " pierde " + (lpEnemigoAntes - enemigo.getVida()) + " LP  ->  "
                    + enemigo.getVida() + " LP restantes");
        }
        if (actual.getVida() < lpPropioAntes) {
            vista.mostrarMensaje("  -> " + actual.getNombre() + " pierde " + (lpPropioAntes - actual.getVida()) + " LP (daño de rebote)");
        }

        vista.actualizarEstado(juego);
        verificarFin();
    }

    public void accionTerminarTurno() {
        vista.mostrarMensaje("\n[ Fase Final — "
                + juego.getJugadorActual().getNombre() + " ]");

        juego.faseFinal(); // cambia el turno, revierte efectos temporales
        yaRoboEsteTurno = false;

        String nombreSiguiente = juego.getJugadorActual().getNombre();
        vista.mostrarMensaje("  Turno terminado.  Ahora le toca a: " + nombreSiguiente);
        vista.actualizarEstado(juego);
    }

    public boolean verificarFin() {
        if (!juego.hayGanador()) return false;
        vista.mostrarGanador(juego);
        return true;
    }

    public void iniciarBucleConsola() {
        vistaConsola cv = (vista instanceof vistaConsola) ? (vistaConsola) vista : null;

        vista.mostrarMensaje("╔═════════════════════════════════════════════════╗");
        vista.mostrarMensaje("║           ¡¡DUELO INICIADO!!                    ║");
        vista.mostrarMensaje("║  " + juego.getJugador1().getNombre()
                + "  VS  " + juego.getJugador2().getNombre());
        vista.mostrarMensaje("║  ¡" + juego.getJugadorActual().getNombre() + " va primero!");
        vista.mostrarMensaje("╚═════════════════════════════════════════════════╝");

        while (!juego.hayGanador()) {
            Jugador actual = juego.getJugadorActual();

            if (cv != null) {
                cv.esperarEnter("══ Es el turno de " + actual.getNombre() + " ══");
            }

            vista.actualizarEstado(juego);

            if (!accionRobar()) break;
            if (verificarFin()) break;

            boolean turnoTerminado = false;
            while (!turnoTerminado && !juego.hayGanador()) {

                String[] opciones = {
                    "Jugar carta de mano",
                    "Declarar ataque",
                    "Ver cementerio",
                    "Terminar turno"
                };
                int accion = vista.elegirOpcionMenu(
                        "Turno de " + actual.getNombre(), opciones);

                switch (accion) {
                    case 0: 
                        if (cv != null) {
                            int idx = cv.elegirCartaDeMano(actual);
                            if (idx >= 0) accionJugarCarta(actual.getMano().get(idx));
                        }
                        break;

                    case 1:  
                        accionAtacar();
                        break;

                    case 2: 
                        vista.mostrarInfo("Cementerio",
                                buildCementerioStr(juego.getJugador1(), juego.getJugador2()));
                        break;

                    case 3:
                        turnoTerminado = true;
                        break;

                    default:
                        
                        break;
                }
            }

            if (juego.hayGanador()) break;

            accionTerminarTurno();
        }

        if (!verificarFin()) vista.mostrarGanador(juego); 

        if (vista.ofrecerNuevoDuelo()) {
            reiniciarDuelo(cv);
        } else {
            vista.mostrarMensaje("  ¡Hasta la próxima! Cerrando...");
            System.exit(0);
        }
    }

    private void procesarInvocacion(Monstruo monstruo, Jugador actual) {
        vista.mostrarMensaje("\n[ Invocación: " + monstruo.getNombre() + " ]");

        if (monstruo.necesitaSacrificio()) {
            if (actual.getCampo().isEmpty()) {
                vista.mostrarMensaje("  " + monstruo.getNombre() + " es Nivel " + monstruo.getNivel() + " y necesita sacrificio." + " ¡No tienes monstruos en campo!");
                return;
            }

            Monstruo sacrificio = vista.elegirMonstruo(
                    new ArrayList<>(actual.getCampo()),
                    monstruo.getNombre() + " (Nivel " + monstruo.getNivel() + ") necesita sacrificio. Elige monstruo a sacrificar");
            if (sacrificio == null) {
                vista.mostrarMensaje("  Invocación cancelada.");
                return;
            }

            boolean ok = actual.invocarMonstruo(monstruo, sacrificio);
            if (ok) {
                vista.mostrarMensaje( sacrificio.getNombre() + " sacrificado.");
                vista.mostrarMensaje( monstruo.getNombre() + " invocado!" + "  ATK:" + monstruo.getAtk() + "  DEF:" + monstruo.getDef() + "  Nivel:" + monstruo.getNivel());
            }
        } else {
            boolean ok = actual.invocarMonstruo(monstruo);
            if (ok) {
                vista.mostrarMensaje(monstruo.getNombre() + " invocado al campo!" + "  ATK:" + monstruo.getAtk() + "  DEF:" + monstruo.getDef());
            }
        }
    }

    private void procesarMagia(CartaMagica carta, Jugador actual, Jugador enemigo) {
        vista.mostrarMensaje("\n[ Carta Mágica: " + carta.getNombre() + " ]");
        vista.mostrarMensaje("  Efecto: " + carta.getDescripcion());

        Contexto ctx = new Contexto(actual, enemigo);
        ctx.setJuego(juego);

        if (carta.necesitaMonstruoPropio()) {
            if (actual.getCampo().isEmpty()) {
                vista.mostrarMensaje("  Esta magia necesita un monstruo propio en campo.");
                return;
            }
            Monstruo objetivo = vista.elegirMonstruo(
                    new ArrayList<>(actual.getCampo()),
                    "Elige tu monstruo objetivo para " + carta.getNombre());
            if (objetivo == null) { vista.mostrarMensaje("  Cancelado."); return; }
            ctx.setMonstruoPropio(objetivo);
        }

        if (carta.necesitaMonstruoEnemigo()) {
            if (enemigo.getCampo().isEmpty()) {
                vista.mostrarMensaje("  Esta magia necesita un monstruo enemigo en campo.");
                return;
            }
            Monstruo objetivo = vista.elegirMonstruo(
                    new ArrayList<>(enemigo.getCampo()),
                    "Elige el monstruo enemigo objetivo para " + carta.getNombre());
            if (objetivo == null) { vista.mostrarMensaje("  Cancelado."); return; }
            ctx.setMonstruoEnemigo(objetivo);
        }

        boolean exito = actual.jugarMagia(carta);
        if (exito) {
            carta.activar(ctx);
            vista.mostrarMensaje("  ¡Magia activada con éxito!");
        }
    }

    private void procesarColocarTrampa(CartaTrampa trampa, Jugador actual) {
        vista.mostrarMensaje("\n[ Colocar Trampa ]");
        boolean ok = actual.colocarTrampa(trampa);
        if (ok) {
            vista.mostrarMensaje("  Trampa colocada boca abajo. ¡El oponente no sabe qué es!");
        }
    }

    public Contexto procesarTrampasEnemigas(Jugador defensivo, Jugador atacante, Monstruo mAtacante) {
        List<CartaTrampa> disponibles = new ArrayList<>();
        for (CartaTrampa t : defensivo.getTrampas()) {
            if (!t.fueActivada()) disponibles.add(t);
        }

        if (disponibles.isEmpty()) return null;


        vista.mostrarMensaje("\n  ► " + defensivo.getNombre()
                + ", tienes trampas disponibles. ¿Deseas activar alguna?");

        CartaTrampa elegida = vista.elegirTrampa(
                disponibles,
                defensivo.getNombre() + " — Activar trampa (fase de trampas)");

        if (elegida == null) return null;

        Contexto ctx = new Contexto(defensivo, atacante, null, mAtacante);
        ctx.setJuego(juego);
        elegida.activar(ctx);

        defensivo.getTrampas().remove(elegida);
        defensivo.agregarAlCementerio(elegida);

        vista.mostrarMensaje("  ¡¡ TRAMPA ACTIVADA !! → " + elegida.getNombre());
        vista.mostrarMensaje("     " + elegida.getDescripcion());

        return ctx;
    }

    private String buildCementerioStr(Jugador j1, Jugador j2) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Cementerio de ").append(j1.getNombre()).append(" ===\n");
        if (j1.getCementerio().isEmpty()) {
            sb.append("  (vacío)\n");
        } else {
            for (Carta c : j1.getCementerio()) {
                sb.append("  · ").append(c.getNombre()).append(" [").append(c.getTipo()).append("]\n");
            }
        }
        sb.append("\n=== Cementerio de ").append(j2.getNombre()).append(" ===\n");
        if (j2.getCementerio().isEmpty()) {
            sb.append("  (vacío)\n");
        } else {
            for (Carta c : j2.getCementerio()) {
                sb.append("  · ").append(c.getNombre()).append(" [").append(c.getTipo()).append("]\n");
            }
        }
        return sb.toString();
    }

    private void reiniciarDuelo(vistaConsola cv) {

        if (cv == null) return;
        cv.esperarEnter("Iniciando nuevo duelo...");
        cv.mostrarMensaje("Ingresa el nombre del Duelista 1: ");
        System.exit(0); 
    }

    public void guardarPartida() {

    PERSISTENCIA.guardadorPartida.guardar(juego);

    vista.mostrarMensaje("Partida guardada correctamente.");
    }

}
