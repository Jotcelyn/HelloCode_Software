package GestionAprendizaje_Modulo.Fabrica;

import java.util.ArrayList;
import java.util.List;

import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.Ruta;

public class RutaFactory {
    public static Ruta crearRuta(String nombre, String descripcion, List<Leccion> listaLecciones) {
        List<Leccion> leccionesClasificadas = new ArrayList<>();

        // Clasificar las lecciones automáticamente (ejemplo: por lenguaje)
        for (Leccion leccion : listaLecciones) {
            if (leccion.getLenguaje().equalsIgnoreCase(nombre)) {
                leccionesClasificadas.add(leccion);
            }
        }

        return new Ruta(nombre, descripcion, leccionesClasificadas);
    }
}
