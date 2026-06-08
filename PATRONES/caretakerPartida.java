package PATRONES;

import java.util.Stack;

public class caretakerPartida {

    private Stack<mementoPartida> historial = new Stack<>();

    public void guardar(mementoPartida memento) {

        historial.push(memento);
    }

    public mementoPartida recuperar() {

        if (historial.isEmpty()) {

            return null;
        }

        return historial.pop();
    }
}