package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import GestionAprendizaje_Modulo.Servicio.DatosManager;
import GestionAprendizaje_Modulo.Servicio.LeccionProvider;
import GestorEjercicios.enums.LenguajeProgramacion;
import GestorEjercicios.enums.NivelDificultad;
import GestorEjercicios.model.Leccion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para el diálogo (ventana emergente) que permite al administrador
 * crear un nuevo nodo para una ruta. Se encarga de "jalar" y filtrar las lecciones
 * relevantes del módulo de ejercicios.
 */
public class DialogoCrearNodoController {

    // --- Componentes FXML de la Vista ---
    @FXML private ComboBox<Leccion> comboLecciones;
    @FXML private ListView<RecursoAprendizaje> listRecursosAñadidos;
    @FXML private ComboBox<String> comboTipoRecurso;
    @FXML private TextField txtTituloRecurso;
    @FXML private TextField txtUrlRecurso;
    @FXML private TextField txtDatoExtraRecurso;
    @FXML private Button btnGuardar;

    // --- Conexión a los Servicios Centrales ---
    private final LeccionProvider leccionProvider = LeccionProvider.getInstancia();
    private final DatosManager datosManager = DatosManager.getInstancia();

    // --- Variables de Estado ---
    private Ruta rutaActual;
    private Curso cursoActual;
    private final ObservableList<RecursoAprendizaje> recursosTemporales = FXCollections.observableArrayList();

    /**
     * Método público para inyectar el contexto (Curso y Ruta) desde el controlador principal.
     * Este es el punto de entrada de datos para este diálogo.
     * @param curso El curso que se está editando.
     * @param ruta La ruta específica a la que se añadirá el nodo.
     */
    public void setData(Curso curso, Ruta ruta) {
        this.cursoActual = curso;
        this.rutaActual = ruta;
        // Una vez que tenemos el contexto, cargamos y filtramos las lecciones.
        cargarYFiltrarLeccionesDisponibles();
    }

    /**
     * Se ejecuta una sola vez cuando se carga el FXML.
     * Ideal para configurar la apariencia estática de los componentes.
     */
    @FXML
    public void initialize() {
        listRecursosAñadidos.setItems(recursosTemporales);
        comboTipoRecurso.setItems(FXCollections.observableArrayList("Video", "PDF", "Artículo"));

        // Se utiliza un CellFactory para un control total y robusto sobre cómo se muestra cada lección.
        Callback<ListView<Leccion>, ListCell<Leccion>> cellFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Leccion leccion, boolean empty) {
                super.updateItem(leccion, empty);
                if (empty || leccion == null) {
                    setText(null);
                } else {
                    // Texto que aparecerá en la lista desplegable del ComboBox
                    setText(String.format("%s (%s)", leccion.getNombre(), leccion.getDificultad()));
                }
            }
        };

        comboLecciones.setCellFactory(cellFactory);
        comboLecciones.setButtonCell(cellFactory.call(null)); // Asegura que el texto se muestre también cuando está cerrado
    }

    /**
     * Obtiene todas las lecciones del LeccionProvider y las filtra
     * basándose en el lenguaje del curso y la dificultad de la ruta.
     */
    private void cargarYFiltrarLeccionesDisponibles() {
        if (cursoActual == null || rutaActual == null) return;

        // 1. Traduce los nombres de tu módulo a los Enums del otro módulo
        LenguajeProgramacion lenguajeBuscado = mapearNombreCursoALenguaje(cursoActual.getNombre());
        NivelDificultad dificultadBuscada = mapearNombreRutaADificultad(rutaActual.getNombre());

        if (lenguajeBuscado == null || dificultadBuscada == null) {
            comboLecciones.setPromptText("Mapeo de Curso/Ruta inválido");
            comboLecciones.setDisable(true);
            return;
        }

        // 2. "Jala" todas las lecciones existentes desde el módulo externo
        List<Leccion> todasLasLecciones = leccionProvider.getTodasLasLecciones();

        // 3. Filtra la lista según los criterios de lenguaje y dificultad
        List<Leccion> leccionesFiltradas = todasLasLecciones.stream()
                .filter(leccion -> leccion.getLenguaje() == lenguajeBuscado)
                .filter(leccion -> leccion.getDificultad() == dificultadBuscada)
                .collect(Collectors.toList());

        // 4. Muestra las lecciones filtradas en el ComboBox
        comboLecciones.setItems(FXCollections.observableArrayList(leccionesFiltradas));

        if (leccionesFiltradas.isEmpty()) {
            comboLecciones.setPromptText("No hay lecciones para este nivel");
            comboLecciones.setDisable(true);
        } else {
            comboLecciones.setPromptText("Seleccione una lección para vincular...");
            comboLecciones.setDisable(false);
        }
    }

    // --- Métodos de Mapeo (Traductores entre módulos) ---
    private LenguajeProgramacion mapearNombreCursoALenguaje(String nombreCurso) {
        try {
            return LenguajeProgramacion.valueOf(nombreCurso.trim().toUpperCase());
        } catch (IllegalArgumentException e) { return null; }
    }

    private NivelDificultad mapearNombreRutaADificultad(String nombreRuta) {
        switch (nombreRuta.trim().toLowerCase()) {
            case "principiante": return NivelDificultad.BASICO;
            case "intermedio": return NivelDificultad.INTERMEDIO;
            case "avanzado": return NivelDificultad.AVANZADO;
            default: return null;
        }
    }

    // --- Manejadores de Eventos de la UI ---

    @FXML
    void handleGuardarNodo(ActionEvent event) {
        Leccion leccionSeleccionada = comboLecciones.getValue();
        if (leccionSeleccionada == null) {
            new Alert(Alert.AlertType.ERROR, "Debe seleccionar una lección para vincular al nodo.").show();
            return;
        }

        int orden = rutaActual.getNodos().size() + 1;
        NodoRuta nuevoNodo = new NodoRuta(orden, leccionSeleccionada);
        recursosTemporales.forEach(nuevoNodo::agregarMaterialDeApoyo);

        // Le decimos al manager que guarde el nuevo nodo en memoria y en archivo.
        datosManager.guardarNuevoNodo(nuevoNodo, rutaActual);

        cerrarVentana(event);
    }

    @FXML
    void handleAgregarRecurso(ActionEvent event) {
        String tipo = comboTipoRecurso.getValue();
        String titulo = txtTituloRecurso.getText().trim();
        String url = txtUrlRecurso.getText().trim();
        String extra = txtDatoExtraRecurso.getText().trim();

        if (tipo == null || titulo.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Seleccione un tipo y escriba un título.").show();
            return;
        }

        try {
            RecursoAprendizaje recurso = null;
            switch (tipo) {
                case "Video": recurso = new Video(titulo, url, Integer.parseInt(extra)); break;
                case "PDF": recurso = new DocumentoPDF(titulo, url, Integer.parseInt(extra)); break;
                case "Artículo": recurso = new Articulo(titulo, url); break;
            }
            if (recurso != null) {
                recursosTemporales.add(recurso);
                limpiarCamposRecurso();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El dato extra debe ser un número válido para Videos y PDFs.").show();
        }
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        cerrarVentana(event);
    }

    private void limpiarCamposRecurso() {
        comboTipoRecurso.getSelectionModel().clearSelection();
        txtTituloRecurso.clear();
        txtUrlRecurso.clear();
        txtDatoExtraRecurso.clear();
    }

    private void cerrarVentana(ActionEvent event) {
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }
}