package PERSISTENCIA;

import juego.Juego;
import jugadores.Jugador;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import cartas.Carta;
import cartas.Monstruo;
import java.time.LocalDateTime;


public class guardadorPartida {

    public static void guardar(Juego juego) {

    try {

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("partida.txt")
        );

        Jugador j1 = juego.getJugador1();
        Jugador j2 = juego.getJugador2();

        writer.write("[JUEGO]");
        writer.newLine();
        writer.write("turno=" + juego.getJugadorActual().getNombre());
        writer.newLine();
        writer.write("turnosJugados=" + juego.getTurnos());
        writer.newLine();
        writer.write("fechaGuardado=" + LocalDateTime.now());
        writer.newLine();
        writer.newLine();

        writer.write("[JUGADOR1]");
        writer.newLine();
        writer.write("nombre=" + j1.getNombre());
        writer.newLine();
        writer.write("lp=" + j1.getVida());
        writer.newLine();
        writer.write("mano=" + cartasAString(j1.getMano()));
        writer.newLine();
        writer.write("campo=" + monstruosAString(j1.getCampo()));
        writer.newLine();
        writer.write("cementerio=" + cartasAString(j1.getCementerio()));
        writer.newLine();   
        writer.write("mazo=" + cartasAString(j1.getMazo()));
        writer.newLine();
        writer.write("trampas=" + cartasAString(j1.getTrampas()));
        writer.newLine();  

        writer.write("[JUGADOR2]");
        writer.newLine();
        writer.write("nombre=" + j2.getNombre());
        writer.newLine();
        writer.write("lp=" + j2.getVida());
        writer.newLine();
        writer.write("mano=" + cartasAString(j2.getMano()));
        writer.newLine();
        writer.write("campo=" + monstruosAString(j2.getCampo()));
        writer.newLine();
        writer.write("cementerio=" + cartasAString(j2.getCementerio()));
        writer.newLine();
        writer.write("mazo=" + cartasAString(j2.getMazo()));
        writer.newLine();
        writer.write("trampas=" + cartasAString(j2.getTrampas()));
        writer.newLine();

        writer.close();

        System.out.println("Partida guardada correctamente.");
        historialGuardados.registrarGuardado();

    } catch (IOException e) {

        System.out.println("Error al guardar la partida.");
        e.printStackTrace();
    }
}

private static String cartasAString( java.util.List<? extends Carta> cartas) {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < cartas.size(); i++) {

        sb.append(cartas.get(i).getNombre());

        if (i < cartas.size() - 1) {
            sb.append("|");
        }
    }

    return sb.toString();
}

private static String monstruosAString(java.util.List<Monstruo> monstruos) {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < monstruos.size(); i++) {

        sb.append(monstruos.get(i).getNombre());

        if (i < monstruos.size() - 1) {
            sb.append("|");
        }
    }

    return sb.toString();
}
    
    
}
