package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestorEjercicios.model.Leccion; // <-- Cambia el import
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NodoDetalleController {

    @FXML private Label labelTituloLeccion;
    @FXML private Text textDescripcionLeccion;
    @FXML private VBox vboxRecursos;
    @FXML private Button btnIrALeccion;

    private Leccion leccionActual; // <-- Ahora es la del módulo de ejercicios

    /**
     * Método público para "inyectar" el nodo cuyos detalles se mostrarán.
     * @param nodo El objeto NodoRuta que el usuario seleccionó.
     */
    public void setNodo(NodoRuta nodo) {
        if (nodo == null) return;

        this.leccionActual = nodo.getLeccion();

        // 1. Poblar la información de la Lección
        labelTituloLeccion.setText(leccionActual.getNombre());
        textDescripcionLeccion.setText(""); // O deja vacío si quieres evitar errores

        // 2. Poblar dinámicamente el material de apoyo
        vboxRecursos.getChildren().clear();
        if (nodo.getMaterialDeApoyo().isEmpty()) {
            vboxRecursos.getChildren().add(new Label("No hay material de apoyo para esta lección."));
        } else {
            for (RecursoAprendizaje recurso : nodo.getMaterialDeApoyo()) {
                VBox cardRecurso = new VBox(3);
                Label tituloRecurso = new Label(recurso.getTitulo());
                tituloRecurso.setStyle("-fx-font-weight: bold;");
                Text detalleRecurso = new Text(recurso.obtenerDetalle());
                detalleRecurso.setWrappingWidth(340);
                cardRecurso.getChildren().addAll(tituloRecurso, detalleRecurso);
                vboxRecursos.getChildren().add(cardRecurso);
            }
        }
    }

    /**
     * --- MÉTODO NUEVO: Se ejecuta cuando el usuario hace clic en el botón "Ir a la Lección" ---
     * @param event El evento del clic.
     */
    @FXML
    void handleIrALeccion(ActionEvent event) {
        if (this.leccionActual != null) {
            // Aquí deberías abrir la vista del módulo de ejercicios y pasar la lección
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Navegación a Módulo Externo");
            alert.setHeaderText("Abriendo la lección: " + leccionActual.getNombre());
            alert.setContentText("En este punto, la aplicación cargaría la vista de ejercicios correspondiente a esta lección.");
            alert.showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "No se ha podido cargar la información de la lección.").show();
        }
    }
}