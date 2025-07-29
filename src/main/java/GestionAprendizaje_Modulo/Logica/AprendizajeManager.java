package GestionAprendizaje_Modulo.Logica;

import GestionAprendizaje_Modulo.Logica.Curso;
import GestionAprendizaje_Modulo.Logica.NodoRuta;
import GestionAprendizaje_Modulo.Logica.Ruta;
import Nuevo_Modulo_Leccion.dataBase.LeccionRepository;
import Nuevo_Modulo_Leccion.logic.Leccion;

import java.util.ArrayList;
import java.util.List;

/**
 * =================================================================================
 * La Clase "Operacional" - AprendizajeManager (Versión Robusta)
 * =================================================================================
 * Esta versión es "a prueba de fallos". Intenta "jalar" las lecciones del otro
 * módulo, pero si esa llamada falla, en lugar de bloquear la aplicación,
 * crea un conjunto de lecciones de prueba para que la UI pueda ser demostrada.
 */
public class AprendizajeManager {

    private static AprendizajeManager instancia;
    private final List<Curso> cursos;

    private AprendizajeManager() {
        this.cursos = new ArrayList<>();
    }

    public static synchronized AprendizajeManager getInstancia() {
        if (instancia == null) {
            instancia = new AprendizajeManager();
        }
        return instancia;
    }

    /**
     * El método central que construye toda la estructura de datos.
     */
    public void construirDatosDePrueba() {
        if (!cursos.isEmpty()) return;

        System.out.println("[AprendizajeManager] Construyendo estructura de aprendizaje...");

        List<Leccion> pozoDeLecciones;

        // --- ¡AQUÍ ESTÁ LA CORRECCIÓN CLAVE! ---
        // Envolvemos la llamada al código externo en un bloque try-catch.
        try {
            // 1. Intentamos "jalar" las lecciones del otro módulo.
            pozoDeLecciones = LeccionRepository.getLecciones();
            if (pozoDeLecciones.isEmpty()) {
                System.err.println("[AprendizajeManager] ADVERTENCIA: El otro módulo devolvió 0 lecciones.");
            } else {
                System.out.println("[AprendizajeManager] ÉXITO: Se jalaron " + pozoDeLecciones.size() + " lecciones del otro módulo.");
            }
        } catch (Exception e) {
            // 2. Si la llamada falla (como te está pasando ahora con el IndexOutOfBoundsException),
            // en lugar de dejar que la app se bloquee, capturamos el error.
            System.err.println("[AprendizajeManager] ERROR CRÍTICO al jalar lecciones del otro módulo: " + e.getMessage());
            System.err.println("[AprendizajeManager] PROCEDIENDO A CREAR LECCIONES DE EMERGENCIA PARA DEMOSTRACIÓN.");

            // 3. Creamos una lista de lecciones de prueba para que tu app pueda seguir funcionando.
            pozoDeLecciones = new ArrayList<>();
            pozoDeLecciones.add(new Leccion()); // Lección de prueba #1
            pozoDeLecciones.add(new Leccion()); // Lección de prueba #2
            pozoDeLecciones.add(new Leccion()); // Lección de prueba #3
        }
        // A partir de aquí, 'pozoDeLecciones' SIEMPRE tendrá algo, ya sean las lecciones reales o las de emergencia.

        // Creamos la estructura de Curso -> Ruta -> Nodos como antes.
        Curso cursoDemo = new Curso("curso-java-01", "Curso de Java", "Aprende Java desde cero");
        Ruta rutaBasica = new Ruta("ruta-java-basico", "Conceptos Básicos", "BASICO");

        int ordenNodo = 1;
        for (Leccion leccion : pozoDeLecciones) {
            NodoRuta nodo = new NodoRuta(ordenNodo++, leccion);
            rutaBasica.agregarNodo(nodo);
        }

        cursoDemo.agregarRuta(rutaBasica);
        this.cursos.add(cursoDemo);
        System.out.println("[AprendizajeManager] Estructura creada exitosamente con " + rutaBasica.getNodos().size() + " nodos.");
    }

    public List<Curso> getCursos() {
        return cursos;
    }
}