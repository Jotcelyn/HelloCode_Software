package GestionAprendizaje_Modulo.Controladores;

import java.io.IOException;

import MetodosGlobales.MetodosFrecuentes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class selectorRolController {

    @FXML
    private Button buttonAdmin;
    @FXML
    private void handleAdminButton(ActionEvent event) throws IOException {
        // Carga la vista del administrador
        MetodosFrecuentes.cambiarVentana((Stage) buttonAdmin.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/AdminRutaVisual.fxml","ADMINISTRADOR");

    }
    @FXML
    private Button buttonEstudiante;

    @FXML
    void handleEstudianteButton(ActionEvent event) throws IOException {
        // Carga la vista del estudiante
        MetodosFrecuentes.cambiarVentana((Stage) buttonEstudiante.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/StudentDashboard.fxml","ESTUDIANTE");
    }

}