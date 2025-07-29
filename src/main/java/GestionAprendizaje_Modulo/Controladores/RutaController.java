package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Ruta;
import GestionAprendizaje_Modulo.Repositorio.RutaRepository;

public class RutaController {
    private RutaRepository rutaRepository;

    public RutaController(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    public void mostrarRutas() {
        for (Ruta ruta : rutaRepository.obtenerRutas()) {
            System.out.println("Ruta: " + ruta.getNombre() + " - " + ruta.getDescripcion());
        }
    }
}
