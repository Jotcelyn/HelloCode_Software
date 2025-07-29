package GestionAprendizaje_Modulo.Modelo;

import java.util.List;

public class Curso {
    private String nombre;
    private String descripcion;
    private List<Ruta> rutas;

    public Curso(String nombre, String descripcion, List<Ruta> rutas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutas = rutas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<Ruta> getRutas() {
        return rutas;
    }
}
