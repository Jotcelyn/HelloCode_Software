package GestionAprendizaje_Modulo.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardEstudianteController {

    @FXML
    private ComboBox<String> lenguajeComboBox;

    @FXML
    private ComboBox<String> nivelComboBox;

    @FXML
    private Button continuarButton;

    @FXML
    private Label mensajeLabel;

    @FXML
    private void initialize() {
        lenguajeComboBox.getItems().addAll("Java", "Python", "C", "PHP");
        nivelComboBox.getItems().addAll("Principiante", "Intermedio", "Avanzado");
    }

    @FXML
    private void manejarContinuar() {
        String lenguajeSeleccionado = lenguajeComboBox.getValue();
        String nivelSeleccionado = nivelComboBox.getValue();

        if (lenguajeSeleccionado == null || nivelSeleccionado == null) {
            mensajeLabel.setText("Por favor, selecciona un lenguaje y un nivel.");
        } else {
            mensajeLabel.setText("");
            redirigirARuta(lenguajeSeleccionado);
        }
    }

    private void redirigirARuta(String lenguaje) {
        // Lógica para redirigir a la pantalla de ruta según el lenguaje seleccionado
        Stage stage = (Stage) continuarButton.getScene().getWindow();
        // Cargar la vista de la ruta correspondiente
        // Ejemplo: stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Ruta.fxml"))));
    }
}
