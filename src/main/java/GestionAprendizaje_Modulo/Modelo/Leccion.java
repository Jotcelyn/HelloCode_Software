package GestionAprendizaje_Modulo.Modelo;

public class Leccion {
    private final String id;
    private String titulo;
    private String descripcion;
    private String estado;

    // --- NUEVOS CAMPOS PARA FILTRADO ---
    private String temaCurso; // Ej: "Java", "Python"
    private String nivel;     // Ej: "Principiante", "Intermedio", "Avanzado"

    // Constructor actualizado
    public Leccion(String id, String titulo, String descripcion, String estado, String temaCurso, String nivel) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.temaCurso = temaCurso;
        this.nivel = nivel;
    }

    // Getters para los nuevos campos
    public String getTemaCurso() { return temaCurso; }
    public String getNivel() { return nivel; }

    // ... resto de tus getters y setters existentes ...
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    // ...
}