package GestionAprendizaje_Modulo.Aplicacion;

import Modulo_Ejercicios.DataBase.EjercicioRepository;
import Modulo_Ejercicios.exercises.EjercicioCompletarCodigo;
import Modulo_Ejercicios.exercises.EjercicioSeleccion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI_Contenido extends Application {
    @Override
    public void start(Stage stage)throws Exception{
    FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml"));
    Scene scene=new Scene(fxmlLoader.load());
    stage.setTitle("Rol");//Titulo
    stage.setScene(scene);
    stage.show();
    }
     // Cargar la pantalla de login del módulo de usuario
    //     FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/login.fxml"));
    //     Scene scene = new Scene(fxmlLoader.load(), 360, 720);
    //     stage.setTitle("Hello Code Software - Login");
    //     stage.setScene(scene);
    //     stage.setResizable(false);
    //     stage.show();
    // }
    //   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/Splash.fxml"));
    //     Scene scene = new Scene(fxmlLoader.load());
    //     stage.setTitle("EntradaAplicativo");
    //     stage.setScene(scene);
    //     stage.show();
    // }

    public static void main (String[] args){
        GestorEjercicios.GestorEjerciciosEntry.inicializar();

        // --- INICIO: Crear lecciones automáticamente si no existen ---
        var gestor = GestorEjercicios.GestorEjerciciosEntry.obtenerGestor();
        if (gestor.obtenerTodasLasLecciones().isEmpty()) {
            // Cargar ejercicios desde el módulo de ejercicios
            java.util.List<EjercicioSeleccion> ejerciciosSeleccion = EjercicioRepository.cargarEjerciciosSeleccion();
            java.util.List<EjercicioCompletarCodigo> ejerciciosCompletar = EjercicioRepository.cargarEjerciciosCompletarCodigo();

            // Convertir a List<Object>
            java.util.List<Object> ejerciciosSeleccionObj = new java.util.ArrayList<>(ejerciciosSeleccion);
            java.util.List<Object> ejerciciosCompletarObj = new java.util.ArrayList<>(ejerciciosCompletar);

            // Crear lección de selección múltiple
            if (!ejerciciosSeleccionObj.isEmpty()) {
                gestor.crearLeccion("Lección Selección Múltiple", ejerciciosSeleccionObj, GestorEjercicios.enums.TipoLeccion.NORMAL, 15, 5);
            }
            // Crear lección de completar código
            if (!ejerciciosCompletarObj.isEmpty()) {
                gestor.crearLeccion("Lección Completar Código", ejerciciosCompletarObj, GestorEjercicios.enums.TipoLeccion.NORMAL, 15, 5);
            }
            // Crear lección mixta si hay de ambos tipos
            if (!ejerciciosSeleccionObj.isEmpty() && !ejerciciosCompletarObj.isEmpty()) {
                java.util.List<Object> ejerciciosMixtos = new java.util.ArrayList<>();
                ejerciciosMixtos.addAll(ejerciciosSeleccionObj);
                ejerciciosMixtos.addAll(ejerciciosCompletarObj);
                gestor.crearLeccion("Lección Mixta", ejerciciosMixtos, GestorEjercicios.enums.TipoLeccion.NORMAL, 20, 10);
            }
        }
        // --- FIN: Crear lecciones automáticamente ---

        launch(args);
    }

}