package FACTORY;

import cartas.CartaMagica;
import efectos.Efecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;

public class reflectionFactory {

    public static CartaMagica cargarCartaMagica(String archivo) {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(archivo));

            String nombre = "";
            String descripcion = "";
            String clase = "";
            Integer valor = null;
            String linea;

            while ((linea = reader.readLine()) != null) {

                if (linea.startsWith("nombre=")) {
                    nombre = linea.substring(7);
                }

                else if (linea.startsWith("descripcion=")) {
                    descripcion = linea.substring(12);
                }

                else if (linea.startsWith("clase=")) {
                    clase = linea.substring(6);
                }

                else if (linea.startsWith("valor=")) {
                    valor = Integer.parseInt(
                            linea.substring(6));
                }
            }

            reader.close();

            Class<?> claseEfecto =Class.forName(clase);

            Efecto efecto;

            if (valor == null) {

                efecto = (Efecto) claseEfecto.getDeclaredConstructor().newInstance();

            } else {

                Constructor<?> constructor = claseEfecto .getDeclaredConstructor(int.class);

                efecto = (Efecto) constructor.newInstance(valor);
            }

            return new CartaMagica( nombre, descripcion, efecto);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}