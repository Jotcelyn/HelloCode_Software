package GestionAprendizaje_Modulo.Servicio;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Repositorio.CursoRepository;
import GestionAprendizaje_Modulo.Repositorio.NodoRepository;
import GestionAprendizaje_Modulo.Repositorio.RutaRepository;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import GestorEjercicios.GestorEjerciciosEntry;
import GestorEjercicios.model.Leccion;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Singleton para gestionar el estado de la aplicación.
// Es el ÚNICO que debe hablar con los Repositories y el módulo externo.
public class DatosManager {
    private static final DatosManager INSTANCIA = new DatosManager();

    private List<Curso> cursos; // Caché en memoria de los cursos
    private List<Ruta> rutas;   // Caché en memoria de las rutas

    private DatosManager() {
        // Inicializa el módulo externo y carga todo desde los archivos.
        inicializarTodo();
    }

    public static DatosManager getInstancia() {
        return INSTANCIA;
    }

    /**
     * Orquesta la carga de datos de todos los módulos al inicio.
     */
    public void inicializarTodo() {
        System.out.println(">>> DatosManager: Iniciando carga de datos...");

        // 1. Inicializa el módulo de ejercicios para que carguen sus lecciones.
        GestorEjerciciosEntry.inicializar();
        System.out.println(GestorEjerciciosEntry.obtenerEstadoModulo());

        // 2. Carga TUS cursos desde el archivo DB_Cursos.txt
        this.cursos = CursoRepository.cargarCursos();

        // 3. Carga TUS rutas y las asocia a los cursos en memoria
        this.rutas = RutaRepository.cargarRutas(this.cursos);

        // 4. Obtiene el mapa de Lecciones del módulo externo
        Map<String, Leccion> leccionesMap = GestorEjerciciosEntry.obtenerTodasLasLecciones().stream()
                .collect(Collectors.toMap(
                        leccion -> String.valueOf(leccion.getId()),
                        leccion -> leccion,
                        (leccionExistente, nuevaLeccion) -> leccionExistente
                ));

        // 5. Carga TUS nodos y los asocia a las Rutas y Lecciones en memoria
        NodoRepository.cargarNodos(this.rutas, leccionesMap);

        System.out.println(">>> Carga completa: " + cursos.size() + " cursos y " + rutas.size() + " rutas cargadas en memoria.");
    }

    // --- MÉTODOS PARA QUE LOS CONTROLADORES ACCEDAN A LOS DATOS ---

    public List<Curso> getCursos() {
        return this.cursos;
    }

    public List<Leccion> getTodasLasLecciones() {
        return GestorEjerciciosEntry.obtenerTodasLasLecciones();
    }

    // --- MÉTODOS PARA QUE LOS CONTROLADORES GUARDEN CAMBIOS ---

    public void guardarNuevoCurso(Curso curso) {
        if (curso != null) {
            this.cursos.add(curso); // Actualiza la caché en memoria
            CursoRepository.guardarCurso(curso); // Escribe en el archivo .txt
        }
    }

    public void guardarNuevaRuta(Ruta ruta, Curso curso) {
        if (ruta != null && curso != null) {
            this.rutas.add(ruta);
            if (!curso.getRutas().contains(ruta)) {
                curso.getRutas().add(ruta); // Actualiza la caché en memoria
            }
            RutaRepository.guardarRuta(ruta, curso); // Escribe en el archivo .txt
        }
    }

    public void guardarNuevoNodo(NodoRuta nodo, Ruta ruta) {
        if (nodo != null && ruta != null) {
            if (!ruta.getNodos().contains(nodo)) {
                ruta.getNodos().add(nodo); // Actualiza la caché en memoria
            }
            NodoRepository.guardarNodo(nodo, ruta); // Escribe en el archivo .txt
        }
    }

    /**
     * Guarda el progreso del estudiante (nodos completados).
     * NOTA: Requiere una función para reescribir el archivo de nodos.
     */
    public void guardarProgresoEstudiante() {
        // En tu arquitectura actual, el estado 'completado' de un nodo
        // no se guarda. Para hacerlo, necesitarías modificar NodoRepository
        // para que guarde ese booleano y lo lea.
        // Y aquí llamarías a un método que reescriba el archivo de nodos.
        System.out.println("Simulando guardado de progreso del estudiante (funcionalidad a implementar en NodoRepository).");
    }
}