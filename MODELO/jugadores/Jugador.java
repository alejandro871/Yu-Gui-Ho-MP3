package jugadores;

import java.util.ArrayList;
import java.util.Random;

import cartas.Carta;
import cartas.Monstruo;
import cartas.CartaMagica;
import cartas.CartaTrampa;
import efectos.Contexto;

public class Jugador {

    private static final int VIDA_MAXIMA = 8000;

    private final String nombre;
    private int vida;

    private final ArrayList<Carta>      mano;
    private final ArrayList<Carta>      mazo;
    private final ArrayList<Monstruo>   campo;
    private final ArrayList<Carta>      cementerio;
    private final ArrayList<CartaTrampa> trampas;

    private boolean cartaJugadaEsteTurno;


    public Jugador(String nombre) {
        this.nombre               = nombre;
        this.vida                 = VIDA_MAXIMA;
        this.mano                 = new ArrayList<>();
        this.mazo                 = new ArrayList<>();
        this.campo                = new ArrayList<>();
        this.cementerio           = new ArrayList<>();
        this.trampas              = new ArrayList<>();
        this.cartaJugadaEsteTurno = false;
    }

    // ─── Mazo y mano ─────────────────────────────────────────────────────

    public void agregarCarta(Carta carta) {
        mazo.add(carta);
    }

    public void tomarManoInicial() {
        for (int i = 0; i < 5; i++) {
            if (!mazo.isEmpty()) mano.add(mazo.remove(0));
        }
        System.out.println(nombre + " toma su mano inicial de 5 cartas.");
    }

    public boolean robarCarta() {
        if (mazo.isEmpty()) {
            System.out.println("*** " + nombre + " intenta robar pero su mazo está VACÍO. ¡DERROTA! ***");
            this.vida = 0;
            return false;
        }
        Carta robada = mazo.remove(0);
        mano.add(robada);
        System.out.println(nombre + " roba: " + robada.getNombre() + "  |  Cartas en mazo: " + mazo.size());
        return true;
    }

    public void recibirDanio(int danio) {
        int danioReal = Math.max(0, danio);
        setVida(this.vida - danioReal);
        System.out.println(nombre + " recibe " + danioReal + " puntos de daño.  LP restantes: " + vida);
    }

    public void curarLP(int cantidad) {
        int cantReal = Math.max(0, cantidad);

        setVida(this.vida + cantReal);
    }

    public boolean estaEliminado() {
        return vida <= 0;
    }

    public boolean yaJugoCartaEsteTurno() {
        return cartaJugadaEsteTurno;
    }

    public boolean invocarMonstruo(Monstruo monstruo, Monstruo sacrificio) {
        if (!mano.contains(monstruo)) {
            System.out.println("  Error: ese monstruo no está en tu mano.");
            return false;
        }
        if (cartaJugadaEsteTurno) {
            System.out.println("  Ya jugaste una carta este turno. No puedes invocar.");
            return false;
        }

        if (monstruo.necesitaSacrificio()) {
            if (sacrificio == null) {
                System.out.println(monstruo.getNombre() + " es nivel " + monstruo.getNivel() + " y necesita un sacrificio.");
                return false;
            }
            if (!campo.contains(sacrificio)) {
                System.out.println("  El monstruo de sacrificio no está en tu campo.");
                return false;
            }
            System.out.println(sacrificio.getNombre()
                    + " es sacrificado para invocar a " + monstruo.getNombre() + "!");
            eliminarMonstruo(sacrificio);
        }

        mano.remove(monstruo);
        campo.add(monstruo);
        cartaJugadaEsteTurno = true;

        System.out.println( nombre + " invoca: " + monstruo.getNombre()
                + "  ATK:" + monstruo.getAtk()
                + "  DEF:" + monstruo.getDef()
                + "  Nivel:" + monstruo.getNivel());
        return true;
    }

    public boolean invocarMonstruo(Monstruo monstruo) {
        return invocarMonstruo(monstruo, null);
    }

    public boolean jugarMagia(CartaMagica carta) {
        if (cartaJugadaEsteTurno) {
            System.out.println("  Ya jugaste una carta este turno.");
            return false;
        }
        if (!mano.contains(carta)) {
            System.out.println("  Esa carta mágica no está en tu mano.");
            return false;
        }
        mano.remove(carta);
        cementerio.add(carta);
        cartaJugadaEsteTurno = true;
        return true;
    }

    public boolean colocarTrampa(CartaTrampa trampa) {
        if (cartaJugadaEsteTurno) {
            System.out.println("  Ya jugaste una carta este turno.");
            return false;
        }
        if (!mano.contains(trampa)) {
            System.out.println("  Esa trampa no está en tu mano.");
            return false;
        }
        mano.remove(trampa);
        trampas.add(trampa);
        cartaJugadaEsteTurno = true;
        System.out.println("  " + nombre + " coloca una trampa boca abajo.");
        return true;
    }

    public boolean atacarConMonstruo(Monstruo atacante, Jugador enemigo, Monstruo defensor) {
        if (!campo.contains(atacante)) {
            System.out.println("  Ese monstruo no está en tu campo.");
            return false;
        }
        if (!atacante.puedeAtacar()) {
            System.out.println("  " + atacante.getNombre() + " ya atacó este turno.");
            return false;
        }

        atacante.marcarComoAtacado();

        if (enemigo.getCampo().isEmpty()) {
            System.out.println(" Ataque directo! " + atacante.getNombre() + " ataca a " + enemigo.getNombre()
                    + " por " + atacante.getAtk() + " puntos!");
            enemigo.recibirDanio(atacante.getAtk());
            return true;
        }

        if (defensor == null || !enemigo.getCampo().contains(defensor)) {
            System.out.println("  El defensor no está en el campo enemigo.");
            return false;
        }

        System.out.println("   " + atacante.getNombre() + " (ATK:" + atacante.getAtk()
                + ") ataca a " + defensor.getNombre()
                + " (" + (defensor.isEnPosicionAtaque()
                        ? "ATK:" + defensor.getAtk()
                        : "DEF:" + defensor.getDef()) + ")");

        if (defensor.isEnPosicionAtaque()) {
            int dif = atacante.getAtk() - defensor.getAtk();
            if (dif > 0) {
                System.out.println("  " + defensor.getNombre() + " destruido! "
                        + enemigo.getNombre() + " pierde " + dif + " LP.");
                enemigo.eliminarMonstruo(defensor);
                enemigo.recibirDanio(dif);
            } else if (dif < 0) {
                System.out.println("  " + atacante.getNombre() + " destruido! "
                        + nombre + " pierde " + (-dif) + " LP.");
                this.eliminarMonstruo(atacante);
                this.recibirDanio(-dif);
            } else {
                System.out.println("  ¡Empate! Ambos monstruos destruidos.");
                this.eliminarMonstruo(atacante);
                enemigo.eliminarMonstruo(defensor);
            }
        } else {
            int dif = atacante.getAtk() - defensor.getDef();
            if (dif > 0) {
                System.out.println("  " + defensor.getNombre() + " en defensa, destruido!");
                enemigo.eliminarMonstruo(defensor);
            } else if (dif < 0) {
                System.out.println("  Defensa resistida. "
                        + nombre + " pierde " + (-dif) + " LP.");
                this.recibirDanio(-dif);
            } else {
                System.out.println("  El ataque no surte efecto (empate con la DEF).");
            }
        }
        return true;
    }

    public void eliminarMonstruo(Monstruo monstruo) {
        if (campo.remove(monstruo)) {
            cementerio.add(monstruo);
            System.out.println("  " + monstruo.getNombre() + " enviado al cementerio de " + nombre + ".");
        }
    }

    public void reiniciarTurno() {
        cartaJugadaEsteTurno = false;
        for (Monstruo m : campo) {
            m.reiniciarParaTurno();
        }
    }


    public void agregarAMano(Carta carta) {
        if (carta != null) mano.add(carta);
    }

    public void agregarAlCementerio(Carta carta) {
        if (carta != null) cementerio.add(carta);
    }

    public Carta recuperarUltimaCartaDelCementerio() {
        if (cementerio.isEmpty()) return null;
        return cementerio.remove(cementerio.size() - 1);
    }

    public Carta descartarCartaAleatoria() {
        if (mano.isEmpty()) return null;
        int idx = new Random().nextInt(mano.size());
        Carta descartada = mano.remove(idx);
        cementerio.add(descartada);
        return descartada;
    }


    public void mostrarMano() {
        System.out.println("--- Mano de " + nombre + " (" + mano.size() + " cartas) ---");
        if (mano.isEmpty()) { System.out.println("  (mano vacía)"); return; }
        for (int i = 0; i < mano.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + mano.get(i));
        }
    }

    public void mostrarCampo() {
        System.out.println("Campo de " + nombre + ":");
        if (campo.isEmpty()) {
            System.out.println("  (sin monstruos)");
        } else {
            for (int i = 0; i < campo.size(); i++) {
                String yaAtaco = campo.get(i).puedeAtacar() ? "" : " [YA ATACÓ]";
                System.out.println("  [" + (i + 1) + "] " + campo.get(i) + yaAtaco);
            }
        }
        if (!trampas.isEmpty()) {
            System.out.println("  Trampas boca abajo: " + trampas.size());
        }
    }

    public String getNombre() { return nombre; }
    public int    getVida() { return vida; }
    public void   setVida(int v) {

        this.vida = Math.max(0, Math.min(VIDA_MAXIMA, v));
    }
    public ArrayList<Carta> getMano() { return mano; }
    public ArrayList<Monstruo> getCampo() { return campo; }
    public ArrayList<Carta> getCementerio() { return cementerio; }
    public ArrayList<CartaTrampa> getTrampas() { return trampas; }
    public int getCartasMazo() { return mazo.size(); }
    public boolean tieneTrampas() { return !trampas.isEmpty(); }
}
