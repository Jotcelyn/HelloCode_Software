package GestionAprendizaje_Modulo.Controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Repositorio.CursoRepository;
import MetodosGlobales.MetodosFrecuentes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private void handleContinuar() {
        // Solo muestra un aviso informativo
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText("Próximamente");
        alert.setContentText("La funcionalidad de continuar con el curso está siendo desarrollada por otro equipo.");
        alert.showAndWait();
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
