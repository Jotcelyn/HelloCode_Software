package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;
import GestionAprendizaje_Modulo.Modelo.Leccion;
// import GestionAprendizaje_Modulo.Modelo.ModuloEducativo;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class DialogoCrearNodoController {

    @FXML private ComboBox<Leccion> comboLecciones;
    @FXML private ListView<RecursoAprendizaje> listRecursosAñadidos;
    @FXML private ComboBox<String> comboTipoRecurso;
    @FXML private TextField txtTituloRecurso;
    @FXML private TextField txtUrlRecurso;
    @FXML private TextField txtDatoExtraRecurso;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Ruta rutaActual;
    private final ObservableList<RecursoAprendizaje> recursosTemporales = FXCollections.observableArrayList();

    /**
     * Este método será llamado desde el controlador padre para inyectar la ruta.
     */
    public void setRuta(Ruta ruta) {
        this.rutaActual = ruta;
        cargarLeccionesDisponibles();
    }

    @FXML
    public void initialize() {
        // Configuración estática de UI
        listRecursosAñadidos.setItems(recursosTemporales);

        comboTipoRecurso.setItems(FXCollections.observableArrayList("Video","PDF","Artículo"));

        comboLecciones.setConverter(new StringConverter<>() {
            @Override public String toString(Leccion lec) {
                return lec == null ? "" : lec.getTitulo();
            }
            @Override public Leccion fromString(String s) { return null; }
        });
    }

    /** Carga las lecciones asociadas al curso de esta ruta */
    private void cargarLeccionesDisponibles() {
        if (rutaActual == null) return;

        // 1) Cargar todos los cursos y encontrar el de esta ruta
        // List<ModuloEducativo> modulos = CursoRepository.cargarCursos().stream()
        //     .filter(c -> c.getId().equals(rutaActual.getCursoId()))
        //     .findFirst()
        //     .map(Curso -> Curso.getModulos())
        //     .orElse(Collections.emptyList());

        // 2) Extraer y aplanar todas las lecciones de esos módulos
        // List<Leccion> lecciones = modulos.stream()
        //     .flatMap(mod -> mod.getLecciones().stream())
        //     .collect(Collectors.toList());

        // 3) Ponerlas en el combo
        // comboLecciones.setItems(FXCollections.observableArrayList(lecciones));
    }

    @FXML
    void handleAgregarRecurso(ActionEvent event) {
        String tipo   = comboTipoRecurso.getValue();
        String titulo = txtTituloRecurso.getText().trim();
        String url    = txtUrlRecurso.getText().trim();
        String extra  = txtDatoExtraRecurso.getText().trim();

        if (tipo == null || titulo.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un tipo y escriba un título.").showAndWait();
            return;
        }

        try {
            RecursoAprendizaje recurso;
            switch (tipo) {
                case "Video":
                    recurso = new Video(titulo, url, Integer.parseInt(extra));
                    break;
                case "PDF":
                    recurso = new DocumentoPDF(titulo, url, Integer.parseInt(extra));
                    break;
                default:
                    recurso = new Articulo(titulo, url);
            }
            recursosTemporales.add(recurso);
            limpiarCamposRecurso();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El dato extra debe ser un número válido.").showAndWait();
        }
    }

    @FXML
    void handleGuardarNodo(ActionEvent event) {
        if (rutaActual == null) {
            new Alert(Alert.AlertType.ERROR, "Ruta no definida.").showAndWait();
            return;
        }
        Leccion leccion = comboLecciones.getValue();
        if (leccion == null) {
            new Alert(Alert.AlertType.ERROR, "Seleccione una lección.").showAndWait();
            return;
        }

        // Crear nodo y agregar recursos
        int orden = rutaActual.getNodos().size() + 1;
        NodoRuta nodo = new NodoRuta(orden, leccion);
        recursosTemporales.forEach(nodo::agregarMaterialDeApoyo);
        rutaActual.agregarNodo(nodo);


        cerrarVentana(event);
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        cerrarVentana(event);
    }

    // ===== Métodos auxiliares =====

    private void limpiarCamposRecurso() {
        comboTipoRecurso.getSelectionModel().clearSelection();
        txtTituloRecurso.clear();
        txtUrlRecurso.clear();
        txtDatoExtraRecurso.clear();
    }

    private void cerrarVentana(ActionEvent event) {
        Stage st = (Stage) ((Button) event.getSource()).getScene().getWindow();
        st.close();
    }
}
