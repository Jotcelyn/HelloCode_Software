package GestionAprendizaje_Modulo.Controladores;

import java.net.URL;
import java.util.ResourceBundle;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Repositorio.RepositorioEnMemoria;
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

    private final RepositorioEnMemoria repo = RepositorioEnMemoria.getInstancia();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Carga todos los cursos que existan en el repositorio
        comboCursos.getItems().addAll(repo.getCursos().values());

   
    }

    @FXML
    private void onCursoSeleccionado() {
        Curso seleccionado = comboCursos.getValue();
        if (seleccionado != null) {
            txtDescripcion.setText(seleccionado.getDescripcion());
        } else {
            txtDescripcion.clear();
        }
    }

    @FXML
    private void handleAtras() {
        // Vuelve a la pantalla de selector de rol
        Stage stage = (Stage) comboCursos.getScene().getWindow();
        MetodosFrecuentes.cambiarVentana(stage,
            "/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml",
            "ROL");
    }
    @FXML private Button btnContinuar;



}
