package GestionAprendizaje_Modulo.Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class RutaController {

    @FXML
    private Pane nodoContainer;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize() {
        double[][] positions = {
            {50, 50}, {150, 120}, {80, 200}, {200, 280}, {120, 360}
        };

        for (int i = 0; i < positions.length; i++) {
            Button nodoButton = new Button("L" + (i + 1));
            nodoButton.setStyle("-fx-background-color: #50C878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 50%; -fx-min-width: 80px; -fx-min-height: 80px; -fx-max-width: 80px; -fx-max-height: 80px; -fx-cursor: hand;");
            nodoButton.setLayoutX(positions[i][0]);
            nodoButton.setLayoutY(positions[i][1]);
            nodoContainer.getChildren().add(nodoButton);
        }
    }

    @FXML
    private void manejarAtras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/DashboardEstudiante.fxml"));
            AnchorPane dashboardPane = loader.load();
            rootPane.getChildren().setAll(dashboardPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRecursos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/Recursos.fxml"));
            AnchorPane recursosPane = loader.load();
            rootPane.getChildren().setAll(recursosPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}