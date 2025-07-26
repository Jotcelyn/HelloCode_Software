package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Repositorio.CursoRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogoCrearCursoController {

    @FXML private TextField txtNombreCurso;
    @FXML private TextArea txtDescripcionCurso;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    // Esta variable guardará el curso creado. Será null si el usuario cancela.
    private Curso nuevoCurso = null;

    /**
     * Método público para que el controlador principal pueda obtener el resultado.
     * @return El Curso creado, o null si se canceló.
     */
    public Curso getNuevoCurso() {
        return nuevoCurso;
    }

    @FXML
    void handleGuardar(ActionEvent event) {
        String nombre = txtNombreCurso.getText();
        String descripcion = txtDescripcionCurso.getText();

        // Validación simple: el nombre no puede estar vacío.
        if (nombre == null || nombre.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Validación");
            alert.setHeaderText(null);
            alert.setContentText("El nombre del curso no puede estar vacío.");
            alert.showAndWait();
            return; // No cierra la ventana si hay un error
        }

        // Si la validación es correcta, crea el objeto Curso.
        this.nuevoCurso = new Curso(nombre, descripcion);
        CursoRepository.guardarCurso(this.nuevoCurso);

        


        // Cierra la ventana del diálogo.
        cerrarVentana();
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        // Simplemente cierra la ventana. nuevoCurso seguirá siendo null.
        cerrarVentana();
    }

    private void cerrarVentana() {
        // Obtiene la referencia al Stage (la ventana) desde cualquier componente, como el botón.
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }
}