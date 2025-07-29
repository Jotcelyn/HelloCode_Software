package GestionAprendizaje_Modulo.Controladores;

import Nuevo_Modulo_Leccion.controllers.LeccionUIController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RutaController {

    @FXML
    private Pane nodoContainer;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize() {
        try {
            // Simulación de posiciones para los nodos
            double[][] positions = {
                {50, 50}, {150, 120}, {80, 200}, {200, 280}, {120, 360}
            };

            for (int i = 0; i < positions.length; i++) {
                final int index = i; // Índice del nodo
                Button nodoButton = new Button("Nodo " + (index + 1));
                nodoButton.setStyle("-fx-background-color: #50C878; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 50%; -fx-min-width: 80px; -fx-min-height: 80px; -fx-max-width: 80px; -fx-max-height: 80px; -fx-cursor: hand;");
                nodoButton.setLayoutX(positions[index][0]);
                nodoButton.setLayoutY(positions[index][1]);

                // Acción al hacer clic en el nodo
                nodoButton.setOnAction(event -> {
                    Stage stage = (Stage) nodoButton.getScene().getWindow();
                    // Simulación de mostrar una lección (aviso)
                    LeccionUIController.mostrarUnaLeccion(null, stage, "/Nuevo_Modulo_Leccion/views/ejemploDeUso.fxml");
                });

                nodoContainer.getChildren().add(nodoButton);
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar RutaController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void manejarAtras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modulo_Usuario/views/homeUsuario.fxml"));
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