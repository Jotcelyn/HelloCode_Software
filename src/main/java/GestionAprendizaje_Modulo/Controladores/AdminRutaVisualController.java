package GestionAprendizaje_Modulo.Controladores;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Repositorio.CursoRepository;
import GestionAprendizaje_Modulo.Repositorio.NodoRepository;
import GestionAprendizaje_Modulo.Repositorio.RutaRepository;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import GestorEjercicios.GestorEjerciciosEntry;
import GestorEjercicios.integracion.IGestorEjercicios;
import GestorEjercicios.model.Leccion;
import MetodosGlobales.MetodosFrecuentes;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AdminRutaVisualController {

    @FXML private ComboBox<Curso> comboCursos;
    @FXML private ComboBox<Ruta> comboRutas;
    @FXML private Button btnNuevaRuta;
    @FXML private VBox pathContainer;
    @FXML private Button buttonAtras;

    private List<Curso> cursos;      // cargados desde TXT
    private List<Ruta> todasRutas;   // cargadas desde TXT
    private Ruta rutaActual;         // la ruta seleccionada actualmente
    @FXML
    public void initialize() {
        configurarComboBoxes();
        // 1) Cargar cursos y rutas desde archivos
        cursos = CursoRepository.cargarCursos();
        todasRutas = RutaRepository.cargarRutas(cursos);

        // 2) Cargar lecciones desde el módulo de ejercicios y asociar nodos
        IGestorEjercicios gestorEjercicios = GestorEjerciciosEntry.obtenerGestor();
        List<Leccion> lecciones = gestorEjercicios.obtenerTodasLasLecciones();
        Map<String, Leccion> mapaLecciones = lecciones.stream()
            .collect(Collectors.toMap(l -> String.valueOf(l.getId()), l -> l));
        NodoRepository.cargarNodos(todasRutas, mapaLecciones);

        comboCursos.setItems(FXCollections.observableArrayList(cursos));
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

    // private void cargarCursos() {
    //     comboCursos.setItems(FXCollections.observableArrayList(repositorio.getCursos().values()));
    // }

    @FXML
        void onCursoSeleccionado(ActionEvent event) {
        Curso curso = comboCursos.getValue();
        comboRutas.getItems().clear();
        pathContainer.getChildren().clear();
        btnNuevaRuta.setDisable(curso == null);
        if (curso != null) {
            // Filtrar SOLO rutas de este curso
            List<Ruta> rutasCurso = todasRutas.stream()
                  .filter(r -> r.getCursoId().equals(curso.getId()))
                  .collect(Collectors.toList());
            comboRutas.setItems(FXCollections.observableArrayList(rutasCurso));
        }
    }
    @FXML
    void onRutaSeleccionada(ActionEvent event) {
    Ruta ruta = comboRutas.getValue();
    this.rutaActual = ruta; // <--- ASIGNAR LA RUTA ACTUAL AQUÍ
    this.pathContainer.getChildren().clear();
    if (ruta != null) {
        ruta.getNodos().forEach(nodo -> pathContainer.getChildren().add(crearComponenteVisualNodo(nodo)));
        // Botón + para añadir nuevo nodo
        Button btnAgregar = new Button("+");
        btnAgregar.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 50;");
        btnAgregar.setPrefSize(50, 50);
        btnAgregar.setOnAction(this::añadirNodos);
        pathContainer.getChildren().add(btnAgregar);
        }
    }

    @FXML
    void handleNuevoCurso(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/GestionAprendizaje_Modulo/Vistas/DialogoCrearCurso.fxml"));
        Parent root = loader.load();
        DialogoCrearCursoController controllerDialogo = loader.getController();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Crear Nuevo Curso");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();

        Curso cursoCreado = controllerDialogo.getNuevoCurso();
        if (cursoCreado != null) {

            // 2) Refrescar el ComboBox con los cursos recién cargados
            //    (vuelve a leer todos los cursos desde el TXT)
            List<Curso> cursosActualizados = CursoRepository.cargarCursos();
            comboCursos.getItems().setAll(cursosActualizados);

            // 3) Seleccionar automáticamente el nuevo curso
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
        Curso curso = comboCursos.getValue();
        if (curso == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione primero un curso.").show();
            return;
        }
        // Diálogo para elegir nivel (princ/interm/avanz)
        List<String> niveles = Arrays.asList("Principiante","Intermedio","Avanzado");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(niveles.get(0), niveles);
        dialog.setTitle("Nueva Ruta");
        dialog.setHeaderText("Seleccione el nivel");
        Optional<String> opt = dialog.showAndWait();
        opt.ifPresent(nivel -> {
            Ruta nueva = new Ruta(UUID.randomUUID().toString(), nivel, "Ruta nivel "+nivel, curso.getId());
            // 1) Guardar en TXT
            RutaRepository.guardarRuta(nueva, curso);
            // 2) Actualizar listas en memoria
            todasRutas.add(nueva);
            comboRutas.getItems().add(nueva);
            comboRutas.getSelectionModel().select(nueva);
        });
    }


    private void actualizarVistaVisual() {
        pathContainer.getChildren().clear();
        if (rutaActual == null) return;

        // Dibujar cada nodo
        for (NodoRuta nodo : rutaActual.getNodos()) {
            pathContainer.getChildren().add(crearComponenteVisualNodo(nodo));
        }
        // Botón para añadir
        Button btnAgregar = new Button("+");
        btnAgregar.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-background-radius:50;");
        btnAgregar.setPrefSize(50,50);
        btnAgregar.setOnAction(this::añadirNodos);
        pathContainer.getChildren().add(btnAgregar);
    }

       void añadirNodos(ActionEvent event) {
        if (rutaActual == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/GestionAprendizaje_Modulo/Vistas/DialogoCrearNodo.fxml"));
            Parent root = loader.load();

            DialogoCrearNodoController ctrl = loader.getController();
            ctrl.setRuta(rutaActual);  // pasas la ruta actual

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Añadir Nuevo Nodo");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            // Después de añadir nodos, vuelves a pintar
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
        Text textoLeccion = new Text(nodo.getLeccion().getNombre());
        textoLeccion.setFont(Font.font("System", 14));
        nodoBox.getChildren().addAll(stack, textoLeccion);
        return nodoBox;
    }

    @FXML
    private void regresarPrincipal() {
        MetodosFrecuentes.cambiarVentana((Stage) buttonAtras.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml","ROL");
    }
}