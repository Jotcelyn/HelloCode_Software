package GestionAprendizaje_Modulo.Controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Repositorio.CursoRepository;
import MetodosGlobales.MetodosFrecuentes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SeleccionCursoEstudianteController implements Initializable {

    @FXML private ComboBox<Curso> comboCursos;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnContinuar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1) Cargar todos los cursos desde el archivo
        List<Curso> cursos = CursoRepository.cargarCursos();
        comboCursos.getItems().setAll(cursos);

        // 2) Inicialmente el botón Continuar está deshabilitado
        btnContinuar.setDisable(true);

    }

     @FXML
    void onCursoSeleccionado(javafx.event.ActionEvent event) {
        Curso seleccionado = comboCursos.getValue();
        if (seleccionado != null) {
            txtDescripcion.setText(seleccionado.getDescripcion());
            btnContinuar.setDisable(false);
        } else {
            txtDescripcion.clear();
            btnContinuar.setDisable(true);
        }
    }    
    // @FXML
    // private void handleContinuar() {
    //     // Aquí pasas a la pantalla de diagnóstico (o siguiente vista)
    //     Stage stage = (Stage) comboCursos.getScene().getWindow();
    //     MetodosFrecuentes.cambiarVentana(
    //         stage,
    //         "/GestionAprendizaje_Modulo/Vistas/DiagnosticoLeccion.fxml",
    //         "DIAGNÓSTICO"
    //     );
    // }

    @FXML
    private void handleContinuar() throws Exception {
        // Cargar la vista del dashboard del estudiante
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/StudentDashboard.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la nueva vista y pasarle la ruta seleccionada
        StudentDashboardController controller = loader.getController();
        // Aquí deberías obtener la ruta del curso seleccionado. Asegúrate de tener un método en el controlador de StudentDashboard
        // que reciba esta información. Por ahora, se deja comentado como referencia.
        // String rutaSeleccionada = ...;
        // controller.setRuta(rutaSeleccionada);

        // Cambiar a la nueva vista
        Stage stage = (Stage) comboCursos.getScene().getWindow();
        MetodosFrecuentes.cambiarVentana(
            stage,
            "/GestionAprendizaje_Modulo/Vistas/StudentDashboard.fxml",
            "DASHBOARD ESTUDIANTE"
        );
    }   

    @FXML
    private void handleAtras() {
        Stage stage = (Stage) comboCursos.getScene().getWindow();
        MetodosFrecuentes.cambiarVentana(
            stage,
            "/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml",
            "ROL"
        );
    }
}
