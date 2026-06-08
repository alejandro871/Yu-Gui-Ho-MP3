package PERSISTENCIA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import cartas.Carta;
import FACTORY.cartaFactory;
import juego.Juego;
import cartas.Monstruo;
import jugadores.Jugador;
import PATRONES.mementoPartida;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        Carta carta = cartaFactory.crearCarta(nombre.trim());
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

                    Carta carta = cartaFactory.crearCarta(nombre.trim());

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

    public static void probarLecturaMano() {

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

                        Carta carta = cartaFactory.crearCarta(nombre.trim());

                        if (carta != null) {

                            System.out.println( carta.getNombre() + " -> " + carta.getTipo() );
                        }
                    }

                    break;
                }
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static java.util.ArrayList<Carta> obtenerManoJugador1() {

        try {

            BufferedReader reader = new BufferedReader( new FileReader("partida.txt"));

            String linea;

            while ((linea = reader.readLine()) != null) {

                if (linea.startsWith("mano=")) {

                    String datos = linea.substring(5);

                    reader.close();

                    return convertirCartas(datos);
                }
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return new java.util.ArrayList<>();
    }

    public static Jugador cargarJugador1() {

        try {

            BufferedReader reader = new BufferedReader( new FileReader("partida.txt"));

            String linea;
            String nombre = "";
            int lp = 8000;
            String datosMano = "";
            String datosCampo = "";
            String datosCementerio = "";
            String datosMazo = "";
            boolean leyendoJugador1 = false;
            String datosTrampas = "";

            while ((linea = reader.readLine()) != null) {

                if (linea.equals("[JUGADOR1]")) {

                    leyendoJugador1 = true;
                    continue;
                }

                if (linea.equals("[JUGADOR2]")) {

                    break;
                }

                if (!leyendoJugador1) {

                    continue;
                }

                if (linea.startsWith("nombre=")) {

                    nombre = linea.substring(7);
                }

                if (linea.startsWith("lp=")) {

                    lp = Integer.parseInt(
                            linea.substring(3));
                }

                if (linea.startsWith("mano=")) {

                    datosMano = linea.substring(5);
                }

                if (linea.startsWith("campo=")) {

                    datosCampo = linea.substring(6);
                }

                if (linea.startsWith("cementerio=")) {

                    datosCementerio = linea.substring(11);
                }

                if (linea.startsWith("mazo=")) {

                    datosMazo = linea.substring(5);
                }

                if (linea.startsWith("trampas=")) {

                    datosTrampas = linea.substring(8);
                }
            }

            reader.close();

            Jugador jugador = new Jugador(nombre);

            jugador.setVida(lp);

            jugador.getMano().addAll( convertirCartas(datosMano));

            for (Carta carta : convertirCartas(datosCampo)) {

                if (carta instanceof Monstruo) {

                    jugador.getCampo().add( (Monstruo) carta);
                }
            }

            jugador.getCementerio().addAll( convertirCartas(datosCementerio));

            jugador.getMazo().addAll( convertirCartas(datosMazo));

            for (Carta carta : convertirCartas(datosTrampas)) {

                if (carta instanceof cartas.CartaTrampa) {

                    jugador.getTrampas().add( (cartas.CartaTrampa) carta);
                }
            }

            return jugador;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

   public static Jugador cargarJugador2() {

    try {

        BufferedReader reader = new BufferedReader( new FileReader("partida.txt"));

        String linea;
        boolean leyendoJugador2 = false;
        String nombre = "";
        int lp = 8000;
        String datosMano = "";
        String datosCampo = "";
        String datosCementerio = "";
        String datosMazo = "";
        String datosTrampas = "";

        while ((linea = reader.readLine()) != null) {

            if (linea.equals("[JUGADOR2]")) {

                leyendoJugador2 = true;
                continue;
            }

            if (!leyendoJugador2) {

                continue;
            }

            if (linea.startsWith("nombre=")) {

                nombre = linea.substring(7);
            }

            if (linea.startsWith("lp=")) {

                lp = Integer.parseInt(linea.substring(3));
            }

            if (linea.startsWith("mano=")) {

                datosMano = linea.substring(5);
            }

            if (linea.startsWith("campo=")) {

                datosCampo = linea.substring(6);
            }

            if (linea.startsWith("cementerio=")) {

                datosCementerio = linea.substring(11);
            }

            if (linea.startsWith("mazo=")) {

                datosMazo = linea.substring(5);
            }

            if (linea.startsWith("trampas=")) {

                datosTrampas = linea.substring(8);
            }
        }

        reader.close();

        Jugador jugador = new Jugador(nombre);

        jugador.setVida(lp);

        jugador.getMano().addAll( convertirCartas(datosMano));

        for (Carta carta : convertirCartas(datosCampo)) {

            if (carta instanceof Monstruo) {

                jugador.getCampo().add( (Monstruo) carta);
            }
        }

        jugador.getCementerio().addAll( convertirCartas(datosCementerio));

        jugador.getMazo().addAll( convertirCartas(datosMazo));

        for (Carta carta : convertirCartas(datosTrampas)) {

            if (carta instanceof cartas.CartaTrampa) {

                jugador.getTrampas().add(
                        (cartas.CartaTrampa) carta);
            }
        }

        return jugador;

    } catch (Exception e) {

        e.printStackTrace();
    }

    return null;
}

    public static Juego cargarJuego() {

    Jugador j1 = cargarJugador1();
    Jugador j2 = cargarJugador2();

    if (j1 == null || j2 == null) {

        System.out.println("Error cargando jugadores.");
        return null;
    }

    Juego juego = new Juego(j1, j2);

    String turnoGuardado = cargarTurno();

    if (turnoGuardado.equals(j1.getNombre())) {

        juego.establecerTurno(j1);

    } else if (turnoGuardado.equals(j2.getNombre())) {

        juego.establecerTurno(j2);
    }

    System.out.println("Partida cargada correctamente.");

    return juego;
    }

    public static mementoPartida crearMemento() {

        try {

            String contenido = Files.readString( Paths.get("partida.txt"));

            return new mementoPartida(contenido);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private static String cargarTurno() {

    try {

        BufferedReader reader = new BufferedReader( new FileReader("partida.txt"));

        String linea;

        while ((linea = reader.readLine()) != null) {

            if (linea.startsWith("turno=")) {

                reader.close();
                return linea.substring(6);
            }
        }

        reader.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return "";
}
}