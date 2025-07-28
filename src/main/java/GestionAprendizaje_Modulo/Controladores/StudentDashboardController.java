package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import GestorEjercicios.model.Leccion;
import Modulo_Ejercicios.Controladores.EjercicioSeleccionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentDashboardController {

    @FXML private VBox pathContainerEstudiante;

    private Ruta rutaActual;

    /**
     * Método para inyectar la ruta a mostrar (llámalo desde el controlador anterior).
     */
    public void setRuta(Ruta ruta) {
        this.rutaActual = ruta;
        mostrarNodosDeRuta();
    }

    /**
     * Muestra los nodos de la ruta en la interfaz.
     */
    private void mostrarNodosDeRuta() {
        pathContainerEstudiante.getChildren().clear();
        if (rutaActual == null) {
            pathContainerEstudiante.getChildren().add(new Text("No hay ruta seleccionada."));
            return;
        }
        for (NodoRuta nodo : rutaActual.getNodos()) {
            if (nodo.getLeccion() == null) continue;
            HBox nodoVisual = crearComponenteVisualNodo(nodo);
            nodoVisual.setOnMouseClicked(e -> abrirLeccionONodo(nodo));
            pathContainerEstudiante.getChildren().add(nodoVisual);
        }
    }

    /**
     * Crea el componente visual (HBox con círculo y texto) para un nodo.
     */
    private HBox crearComponenteVisualNodo(NodoRuta nodo) {
        HBox nodoBox = new HBox(10);
        nodoBox.setAlignment(Pos.CENTER_LEFT);
        nodoBox.setStyle("-fx-background-color: #E8E8E8; -fx-padding: 10; -fx-background-radius: 10; -fx-cursor: hand;");

        String color = nodo.estaCompletado() ? "#4CAF50" : "#2196F3";
        Circle circulo = new Circle(15, Color.web(color));
        Text textoOrden = new Text(nodo.estaCompletado() ? "✔" : String.valueOf(nodo.getOrden()));
        textoOrden.setFont(Font.font("System", 12));
        textoOrden.setFill(Color.WHITE);

        javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(circulo, textoOrden);
        Text textoLeccion = new Text(nodo.getLeccion().getNombre());
        textoLeccion.setFont(Font.font("System", 14));

        nodoBox.getChildren().addAll(stack, textoLeccion);
        return nodoBox;
    }

    /**
     * Abre la lección o ejercicios asociados al nodo seleccionado.
     */
    private void abrirLeccionONodo(NodoRuta nodo) {
        Leccion leccion = nodo.getLeccion();
        if (leccion == null) {
            new Alert(Alert.AlertType.ERROR, "Este nodo no tiene lección asociada.").show();
            return;
        }
        try {
            String fxmlPath = "/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            EjercicioSeleccionController controllerEjercicios = loader.getController();
            // Si tu controlador de ejercicios tiene un método para recibir la lección:
            // controllerEjercicios.setLeccion(leccion);

            Stage stage = new Stage();
            stage.setTitle("Lección: " + leccion.getNombre());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo cargar la lección/ejercicios.").show();
        }
    }
}