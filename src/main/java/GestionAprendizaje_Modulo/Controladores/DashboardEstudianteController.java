package GestionAprendizaje_Modulo.Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class DashboardEstudianteController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ComboBox<String> lenguajeComboBox;

    @FXML
    private ComboBox<String> nivelComboBox;

    @FXML
    private Button continuarButton;

    @FXML
    private void initialize() {
        lenguajeComboBox.getItems().addAll("Java", "Python", "C");
        nivelComboBox.getItems().addAll("Principiante", "Intermedio", "Avanzado");
    }

    @FXML
    private void manejarContinuar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/Ruta.fxml"));
            AnchorPane listaNodosPane = loader.load();
            rootPane.getChildren().setAll(listaNodosPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
