package GestionAprendizaje_Modulo.Modelo;

import java.util.List;

public class Ruta {
    private String nombre;
    private String descripcion;
    private List<Leccion> lecciones;

    public Ruta(String nombre, String descripcion, List<Leccion> lecciones) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.lecciones = lecciones;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<Leccion> getLecciones() {
        return lecciones;
    }

    public void agregarLeccion(Leccion leccion) {
        lecciones.add(leccion);
    }
}
