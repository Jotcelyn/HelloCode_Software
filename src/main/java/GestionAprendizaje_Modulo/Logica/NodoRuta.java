package GestionAprendizaje_Modulo.Logica;
import java.util.ArrayList;
import java.util.List;

/**
 * =================================================================================
 * Clase NodoRuta
 * =================================================================================
 * "El corazón del modelo. Es un paso individual en una Ruta. Combina una
 * Lección con materiales de apoyo." - según UML.
 */
public class NodoRuta {

    private int orden;
    private boolean completado;
    private Leccion leccion;
    private List<RecursoAprendizaje> materialDeApoyo;

    /**
     * Constructor para la clase NodoRuta.
     * @param orden El número de paso de este nodo dentro de la ruta.
     * @param leccion La lección principal asociada a este nodo.
     */
    public NodoRuta(int orden, Leccion leccion) {
        this.orden = orden;
        this.leccion = leccion;
        this.completado = false;
        this.materialDeApoyo = new ArrayList<>(); // Inicializa la lista vacía
    }

    /**
     * Añade un nuevo recurso de aprendizaje a la lista de materiales de este nodo.
     * @param recurso El objeto RecursoAprendizaje que se va a añadir.
     */
    public void agregarMaterialDeApoyo(RecursoAprendizaje recurso) {
        this.materialDeApoyo.add(recurso);
    }

    // Getters y otros métodos
    public int getOrden() {
        return orden;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void marcarComoCompletado() {
        this.completado = true;
    }

    public Leccion getLeccion() {
        return leccion;
    }

    public List<RecursoAprendizaje> getMaterialDeApoyo() {
        return materialDeApoyo;
    }
}