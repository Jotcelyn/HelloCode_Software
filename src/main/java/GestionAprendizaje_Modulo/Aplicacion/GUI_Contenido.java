package GestionAprendizaje_Modulo.Aplicacion;

import GestionAprendizaje_Modulo.Servicio.DatosManager;
import GestorEjercicios.GestorEjerciciosEntry;
import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import Modulo_Ejercicios.DataBase.EjercicioRepository; // Repositorio de ejercicios del otro módulo
import Modulo_Ejercicios.exercises.EjercicioSeleccion; // Clase de ejercicio del otro módulo
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Punto de entrada principal de TODA la aplicación.
 * Orquesta la inicialización de todos los módulos en el orden correcto.
 */
public class GUI_Contenido extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // --- INICIALIZACIÓN CENTRALIZADA Y ORDENADA ---
        System.out.println(">>> MainApp: Iniciando todos los módulos...");

        // 1. INICIALIZAMOS Y POBLAMOS EL MÓDULO DE EJERCICIOS PRIMERO.
        // Esto asegura que las lecciones existan ANTES de que tu módulo las necesite.
        inicializarYPopularModuloEjercicios();

        // 2. AHORA INICIALIZAMOS NUESTRO MÓDULO (el DatosManager).
        // Al crearse, cargará tus cursos/rutas y podrá vincular los nodos
        // a las lecciones que acabamos de crear.
        DatosManager.getInstancia();

        System.out.println(">>> MainApp: Todos los módulos listos. Lanzando UI.");
        // ------------------------------------

        // 3. Carga tu primera vista, el selector de rol.
        Parent root = FXMLLoader.load(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml"));

        primaryStage.setTitle("HelloCode - Sistema de Gestión");
        primaryStage.setScene(new Scene(root, 360, 640));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Este método simula la puesta en marcha del módulo de ejercicios.
     * Carga los ejercicios REALES de su "base de datos" y luego utiliza la API
     * pública (GestorEjerciciosEntry) para crear las lecciones.
     */
    private void inicializarYPopularModuloEjercicios() {
        // Paso 1: Llamar al inicializador oficial del módulo.
        GestorEjerciciosEntry.inicializar();

        // Si el gestor ya tiene lecciones, significa que ya fue poblado. No hacemos nada.
        if (!GestorEjerciciosEntry.obtenerTodasLasLecciones().isEmpty()) {
            System.out.println(">>> Módulo de Ejercicios ya estaba inicializado y poblado.");
            System.out.println(GestorEjerciciosEntry.obtenerEstadoModulo());
            return;
        }

        System.out.println(">>> MainApp: Poblando el Módulo de Ejercicios con lecciones...");

        // Paso 2: Cargar los ejercicios REALES desde el repositorio del otro equipo.
        List<EjercicioSeleccion> todosLosEjercicios = EjercicioRepository.cargarEjerciciosSeleccion();

        // Paso 3: Filtrar esos ejercicios para crear lecciones coherentes.
        List<EjercicioSeleccion> ejerciciosJavaBasico = todosLosEjercicios.stream()
                .filter(e -> e.getLenguaje() == Modulo_Ejercicios.exercises.Lenguaje.JAVA && e.getNivel() == Modulo_Ejercicios.exercises.NivelDificultad.BASICO)
                .collect(Collectors.toList());

        List<EjercicioSeleccion> ejerciciosJavaIntermedio = todosLosEjercicios.stream()
                .filter(e -> e.getLenguaje() == Modulo_Ejercicios.exercises.Lenguaje.JAVA && e.getNivel() == Modulo_Ejercicios.exercises.NivelDificultad.INTERMEDIO)
                .collect(Collectors.toList());

        List<EjercicioSeleccion> ejerciciosJavaAvanzado = todosLosEjercicios.stream()
                .filter(e -> e.getLenguaje() == Modulo_Ejercicios.exercises.Lenguaje.JAVA && e.getNivel() == Modulo_Ejercicios.exercises.NivelDificultad.AVANZADO)
                .collect(Collectors.toList());

        List<EjercicioSeleccion> ejerciciosPythonBasico = todosLosEjercicios.stream()
                .filter(e -> e.getLenguaje() == Modulo_Ejercicios.exercises.Lenguaje.PYTHON && e.getNivel() == Modulo_Ejercicios.exercises.NivelDificultad.BASICO)
                .collect(Collectors.toList());

        // Paso 4: Usar la API pública para crear las lecciones con los ejercicios reales.
        if (!ejerciciosJavaBasico.isEmpty()) {
            GestorEjerciciosEntry.crearLeccionNormal("Fundamentos de Java", ejerciciosJavaBasico, NivelDificultad.BASICO, LenguajeProgramacion.JAVA);
        }
        if (!ejerciciosJavaIntermedio.isEmpty()) {
            GestorEjerciciosEntry.crearLeccionNormal("POO en Java", ejerciciosJavaIntermedio, NivelDificultad.INTERMEDIO, LenguajeProgramacion.JAVA);
        }
        if (!ejerciciosJavaAvanzado.isEmpty()) {
            GestorEjerciciosEntry.crearLeccionPrueba("Patrones de Diseño en Java", ejerciciosJavaAvanzado, NivelDificultad.AVANZADO, LenguajeProgramacion.JAVA);
        }
        if (!ejerciciosPythonBasico.isEmpty()) {
            GestorEjerciciosEntry.crearLeccionNormal("Fundamentos de Python", ejerciciosPythonBasico, NivelDificultad.BASICO, LenguajeProgramacion.PYTHON);
        }

        System.out.println(GestorEjerciciosEntry.obtenerEstadoModulo());
    }

    public static void main(String[] args) {
        launch(args);
    }
}