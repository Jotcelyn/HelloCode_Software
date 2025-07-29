package GestionAprendizaje_Modulo.Controladores;

import java.util.List;

import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class RecursosController {

    @FXML
    private ListView<String> recursosListView;

    @FXML
    private Button abrirRecursoButton;

    @FXML
    private Label mensajeLabel;

    private List<RecursoAprendizaje> recursos;

    public void inicializarRecursos(List<RecursoAprendizaje> recursos) {
        this.recursos = recursos;
        for (RecursoAprendizaje recurso : recursos) {
            recursosListView.getItems().add(recurso.getTitulo());
        }
    }

    @FXML
    private void manejarAbrirRecurso() {
        int indiceSeleccionado = recursosListView.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado < 0) {
            mensajeLabel.setText("Por favor, selecciona un recurso.");
        } else {
            mensajeLabel.setText("");
            RecursoAprendizaje recursoSeleccionado = recursos.get(indiceSeleccionado);
            abrirRecurso(recursoSeleccionado.getUrl());
        }
    }

    private void abrirRecurso(String url) {
        // Lógica para abrir el recurso en el navegador o aplicación correspondiente
        System.out.println("Abriendo recurso: " + url);
    }
}
