package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import GestionAprendizaje_Modulo.Servicio.ServicioDatos;
import MetodosGlobales.MetodosFrecuentes;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminRutaVisualController {

    // --- Componentes FXML ---
    @FXML private ComboBox<Curso> comboCursos;
    @FXML private ComboBox<Ruta> comboRutas;
    @FXML private Button btnNuevaRuta;
    @FXML private VBox pathContainer;
    @FXML private Button buttonAtras;

    // --- Conexión al Servicio de Datos (Singleton) ---
    private final ServicioDatos servicioDatos = ServicioDatos.getInstancia();
    private Ruta rutaActual;

    @FXML
    public void initialize() {
        configurarComboBoxes();
        cargarCursos();
        btnNuevaRuta.setDisable(true);
    }

    private void configurarComboBoxes() {
        comboCursos.setConverter(new StringConverter<>() {
            @Override public String toString(Curso c) { return c == null ? null : c.getNombre(); }
            @Override public Curso fromString(String s) { return null; }
        });
        comboRutas.setConverter(new StringConverter<>() {
            @Override public String toString(Ruta r) { return r == null ? null : r.getNombre(); }
            @Override public Ruta fromString(String s) { return null; }
        });
    }

    private void cargarCursos() {
        comboCursos.setItems(FXCollections.observableArrayList(servicioDatos.getCursos().values()));
    }

    @FXML
    void onCursoSeleccionado(ActionEvent event) {
        Curso cursoSeleccionado = comboCursos.getSelectionModel().getSelectedItem();
        actualizarVistaVisual(null);
        comboRutas.getSelectionModel().clearSelection();
        if (cursoSeleccionado != null) {
            comboRutas.setItems(FXCollections.observableArrayList(cursoSeleccionado.getRutas()));
            btnNuevaRuta.setDisable(false);
        } else {
            comboRutas.getItems().clear();
            btnNuevaRuta.setDisable(true);
        }
    }

    @FXML
    void onRutaSeleccionada(ActionEvent event) {
        this.rutaActual = comboRutas.getSelectionModel().getSelectedItem();
        actualizarVistaVisual(this.rutaActual);
    }

    @FXML
    void handleNuevoCurso(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/DialogoCrearCurso.fxml"));
            Parent root = loader.load();
            DialogoCrearCursoController controllerDialogo = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Crear Nuevo Curso");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(comboCursos.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            Curso cursoCreado = controllerDialogo.getNuevoCurso();
            if (cursoCreado != null) {
                servicioDatos.getCursos().put(cursoCreado.getId(), cursoCreado);
                servicioDatos.save(); // ¡GUARDAR CAMBIOS EN DISCO!
                comboCursos.getItems().add(cursoCreado);
                comboCursos.getSelectionModel().select(cursoCreado);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al abrir el formulario de creación de curso.").show();
        }
    }

    @FXML
    void handleNuevaRuta(ActionEvent event) {
        Curso cursoSeleccionado = comboCursos.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Primero debe seleccionar un curso.").show();
            return;
        }

        List<String> todosLosNiveles = Arrays.asList("Principiante", "Intermedio", "Avanzado");
        List<String> nivelesExistentes = cursoSeleccionado.getRutas().stream().map(Ruta::getNombre).collect(Collectors.toList());
        List<String> nivelesDisponibles = todosLosNiveles.stream().filter(n -> !nivelesExistentes.contains(n)).collect(Collectors.toList());

        if (nivelesDisponibles.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Todas las rutas (Principiante, Intermedio, Avanzado) ya han sido creadas para este curso.").show();
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(nivelesDisponibles.get(0), nivelesDisponibles);
        dialog.setTitle("Crear Nueva Ruta");
        dialog.setHeaderText("Seleccione el nivel para la nueva ruta.");
        dialog.setContentText("Nivel disponible:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombreSeleccionado -> {
            Ruta nuevaRuta = new Ruta(UUID.randomUUID().toString(), nombreSeleccionado, "Ruta de nivel " + nombreSeleccionado);
            cursoSeleccionado.getRutas().add(nuevaRuta);
            servicioDatos.save(); // ¡GUARDAR CAMBIOS EN DISCO!
            comboRutas.getItems().add(nuevaRuta);
            comboRutas.getSelectionModel().select(nuevaRuta);
        });
    }

    private void actualizarVistaVisual(Ruta ruta) {
        this.rutaActual = ruta;
        pathContainer.getChildren().clear();
        if (rutaActual == null) return;

        for (NodoRuta nodo : rutaActual.getNodos()) {
            pathContainer.getChildren().add(crearComponenteVisualNodo(nodo));
        }

        Button btnAgregar = new Button("+");
        btnAgregar.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 50;");
        btnAgregar.setPrefSize(50, 50);
        btnAgregar.setOnAction(this::handleAñadirNodos);
        pathContainer.getChildren().add(btnAgregar);
    }

    private void handleAñadirNodos(ActionEvent event) {
        if (rutaActual == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/DialogoCrearNodo.fxml"));
            Parent root = loader.load();
            DialogoCrearNodoController controllerDialogo = loader.getController();
            controllerDialogo.setData(comboCursos.getSelectionModel().getSelectedItem(), this.rutaActual);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir Nuevo Nodo");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(pathContainer.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            // Al cerrar el diálogo, los cambios ya están en el objeto rutaActual,
            // pero debemos guardarlos en el archivo.
            servicioDatos.save(); // ¡GUARDAR CAMBIOS EN DISCO!

            // Y refrescar la vista visual.
            actualizarVistaVisual(this.rutaActual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HBox crearComponenteVisualNodo(NodoRuta nodo) {
        HBox nodoBox = new HBox(10);
        nodoBox.setAlignment(Pos.CENTER_LEFT);
        nodoBox.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10; -fx-background-radius: 10;");
        Circle circulo = new Circle(15, Color.STEELBLUE);
        Text textoOrden = new Text(String.valueOf(nodo.getOrden()));
        textoOrden.setFont(Font.font("System", 12));
        textoOrden.setFill(Color.WHITE);
        javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(circulo, textoOrden);
        Text textoLeccion = new Text(nodo.getLeccion().getTitulo());
        textoLeccion.setFont(Font.font("System", 14));
        nodoBox.getChildren().addAll(stack, textoLeccion);
        return nodoBox;
    }

    @FXML
    private void regresarPrincipal() {
        MetodosFrecuentes.cambiarVentana((Stage) buttonAtras.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml","ROL");
    }
}