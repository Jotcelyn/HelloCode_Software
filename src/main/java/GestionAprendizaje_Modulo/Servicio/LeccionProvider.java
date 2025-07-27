package GestionAprendizaje_Modulo.Servicio;

import GestorEjercicios.GestorEjerciciosEntry;
import GestorEjercicios.model.Leccion;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LeccionProvider {

    private static final LeccionProvider INSTANCIA = new LeccionProvider();

    private LeccionProvider() {
        // El constructor está ahora vacío. La inicialización y población
        // se maneja en la clase principal de la aplicación.
    }

    public static LeccionProvider getInstancia() {
        return INSTANCIA;
    }

    /**
     * Simplemente "jala" la lista de lecciones que el otro módulo ya tiene.
     */
    public List<Leccion> getTodasLasLecciones() {
        return GestorEjerciciosEntry.obtenerTodasLasLecciones();
    }

    public Map<Integer, Leccion> getLeccionesPorIdMap() {
        return getTodasLasLecciones().stream()
                .collect(Collectors.toMap(Leccion::getId, Function.identity(), (leccion1, leccion2) -> leccion1));
    }
}