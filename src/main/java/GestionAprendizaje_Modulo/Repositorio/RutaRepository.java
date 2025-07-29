package GestionAprendizaje_Modulo.Repositorio;

import java.util.ArrayList;
import java.util.List;

import GestionAprendizaje_Modulo.Modelo.Ruta;

public class RutaRepository {
    private List<Ruta> rutas = new ArrayList<>();

    public void agregarRuta(Ruta ruta) {
        rutas.add(ruta);
    }

    public List<Ruta> obtenerRutas() {
        return rutas;
    }

    public Ruta buscarRutaPorNombre(String nombre) {
        return rutas.stream()
                .filter(ruta -> ruta.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
}
