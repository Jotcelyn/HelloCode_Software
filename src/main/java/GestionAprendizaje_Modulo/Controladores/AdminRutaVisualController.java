package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Repositorio.RepositorioEnMemoria;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
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

    @FXML private ComboBox<Curso> comboCursos;
    @FXML private ComboBox<Ruta> comboRutas;
    @FXML private Button btnNuevaRuta;
    @FXML private VBox pathContainer;
    @FXML private Button buttonAtras;

    private final RepositorioEnMemoria repositorio = RepositorioEnMemoria.getInstancia();
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
        comboCursos.setItems(FXCollections.observableArrayList(repositorio.getCursos().values()));
    }

    @FXML
    void onCursoSeleccionado(ActionEvent event) {
        Curso cursoSeleccionado = comboCursos.getSelectionModel().getSelectedItem();
        pathContainer.getChildren().clear();
        rutaActual = null;
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
        actualizarVistaVisual();
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
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            Curso cursoCreado = controllerDialogo.getNuevoCurso();
            if (cursoCreado != null) {
                repositorio.getCursos().put(cursoCreado.getId(), cursoCreado);
                comboCursos.getItems().add(cursoCreado);
                comboCursos.getSelectionModel().select(cursoCreado);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * --- MÉTODO MODIFICADO ---
     * Ahora usa un ChoiceDialog para ofrecer niveles predefinidos.
     */
    @FXML
    void handleNuevaRuta(ActionEvent event) {
        Curso cursoSeleccionado = comboCursos.getSelectionModel().getSelectedItem();
        if (cursoSeleccionado == null) {
            new Alert(Alert.AlertType.WARNING, "Primero debe seleccionar un curso para añadirle una ruta.").show();
            return;
        }

        // 1. Definir los niveles posibles
        List<String> todosLosNiveles = Arrays.asList("Principiante", "Intermedio", "Avanzado");

        // 2. Obtener los nombres de las rutas que ya existen en este curso
        List<String> nivelesExistentes = cursoSeleccionado.getRutas().stream()
                .map(Ruta::getNombre)
                .collect(Collectors.toList());

        // 3. Filtrar para obtener solo los niveles que AÚN NO se han creado
        List<String> nivelesDisponibles = todosLosNiveles.stream()
                .filter(nivel -> !nivelesExistentes.contains(nivel))
                .collect(Collectors.toList());

        // 4. Comprobar si todavía hay niveles disponibles
        if (nivelesDisponibles.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Todas las rutas (Principiante, Intermedio, Avanzado) ya han sido creadas para este curso.").show();
            return;
        }

        // 5. Crear y mostrar el diálogo de selección (ChoiceDialog)
        ChoiceDialog<String> dialog = new ChoiceDialog<>(nivelesDisponibles.get(0), nivelesDisponibles);
        dialog.setTitle("Crear Nueva Ruta");
        dialog.setHeaderText("Seleccione el nivel para la nueva ruta.");
        dialog.setContentText("Nivel disponible:");

        Optional<String> result = dialog.showAndWait();

        // 6. Si el usuario seleccionó un nivel y presionó OK
        result.ifPresent(nombreSeleccionado -> {
            // Se crea la ruta con el nombre seleccionado
            Ruta nuevaRuta = new Ruta(UUID.randomUUID().toString(), nombreSeleccionado, "Ruta de nivel " + nombreSeleccionado);

            // Se guarda directamente en el curso (que está en el repositorio)
            cursoSeleccionado.getRutas().add(nuevaRuta);

            // Se actualiza la UI para que aparezca la nueva ruta
            comboRutas.getItems().add(nuevaRuta);
            comboRutas.getSelectionModel().select(nuevaRuta);
        });
    }


    private void actualizarVistaVisual() {
        pathContainer.getChildren().clear();
        if (rutaActual == null) return;

        for (NodoRuta nodo : rutaActual.getNodos()) {
            pathContainer.getChildren().add(crearComponenteVisualNodo(nodo));
        }

        Button btnAgregar = new Button("+");
        btnAgregar.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 50;");
        btnAgregar.setPrefSize(50, 50);
        btnAgregar.setOnAction(this::añadirNodos);
        pathContainer.getChildren().add(btnAgregar);
    }

    void añadirNodos(ActionEvent event) {
        if (rutaActual == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAprendizaje_Modulo/Vistas/DialogoCrearNodo.fxml"));
            Parent root = loader.load();
            DialogoCrearNodoController controllerDialogo = loader.getController();
            controllerDialogo.setRuta(this.rutaActual);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir Nuevo Nodo");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            actualizarVistaVisual();
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