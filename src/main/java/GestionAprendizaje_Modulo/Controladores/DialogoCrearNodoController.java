package GestionAprendizaje_Modulo.Controladores;

import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;
import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;
import GestionAprendizaje_Modulo.Servicio.ServicioDatos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;
import java.util.stream.Collectors;

public class DialogoCrearNodoController {

    @FXML private ComboBox<Leccion> comboLecciones;
    @FXML private ListView<RecursoAprendizaje> listRecursosAñadidos;
    @FXML private ComboBox<String> comboTipoRecurso;
    @FXML private TextField txtTituloRecurso;
    @FXML private TextField txtUrlRecurso;
    @FXML private TextField txtDatoExtraRecurso;
    @FXML private Button btnGuardar;

    private final ServicioDatos servicioDatos = ServicioDatos.getInstancia();
    private Ruta rutaActual;
    private ObservableList<RecursoAprendizaje> recursosTemporales = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        listRecursosAñadidos.setItems(recursosTemporales);
        comboTipoRecurso.setItems(FXCollections.observableArrayList("Video", "PDF", "Artículo"));
    }

    /**
     * Recibe el contexto desde el controlador principal.
     * @param cursoContexto El curso seleccionado por el admin.
     * @param ruta La ruta que se está editando.
     */
    public void setData(Curso cursoContexto, Ruta ruta) {
        this.rutaActual = ruta;
        if (cursoContexto != null && ruta != null) {
            filtrarYcargarLecciones(cursoContexto, ruta);
        }
    }

//    private void filtrarYcargarLecciones(Curso curso, Ruta ruta) {
//        String temaCurso = curso.getNombre();
//        String nivelRuta = ruta.getNombre();
//
//        List<Leccion> leccionesFiltradas = servicioDatos.getLecciones().stream()
//                .filter(leccion -> leccion.getTemaCurso().equalsIgnoreCase(temaCurso))
//                .filter(leccion -> leccion.getNivel().equalsIgnoreCase(nivelRuta))
//                .collect(Collectors.toList());
//
//        comboLecciones.setItems(FXCollections.observableArrayList(leccionesFiltradas));
//        comboLecciones.setConverter(new StringConverter<>() {
//            @Override public String toString(Leccion l) { return l == null ? null : l.getTitulo(); }
//            @Override public Leccion fromString(String s) { return null; }
//        });
//    }

    private void filtrarYcargarLecciones(Curso curso, Ruta ruta) {
        String temaCurso = curso.getNombre();
        String nivelRuta = ruta.getNombre();

        // --- INICIO DE LA DEPURACIÓN ---
        System.out.println("\n--- FILTRANDO LECCIONES ---");
        System.out.println("Buscando lecciones para el Curso: '" + temaCurso + "'");
        System.out.println("Y para el Nivel de Ruta: '" + nivelRuta + "'");

        List<Leccion> todasLasLecciones = servicioDatos.getLecciones();
        System.out.println("Total de lecciones en el repositorio: " + todasLasLecciones.size());

        // Imprimimos todas las lecciones disponibles para ver qué datos tienen
        System.out.println("Lecciones disponibles (Tema | Nivel):");
        for (Leccion lec : todasLasLecciones) {
            System.out.println("- " + lec.getTitulo() + " (" + lec.getTemaCurso() + " | " + lec.getNivel() + ")");
        }
        // --- FIN DE LA DEPURACIÓN ---

        List<Leccion> leccionesFiltradas = todasLasLecciones.stream()
                .filter(leccion -> leccion.getTemaCurso().equalsIgnoreCase(temaCurso))
                .filter(leccion -> leccion.getNivel().equalsIgnoreCase(nivelRuta))
                .collect(Collectors.toList());

        System.out.println("Resultado: Se encontraron " + leccionesFiltradas.size() + " lecciones que coinciden.");

        comboLecciones.setItems(FXCollections.observableArrayList(leccionesFiltradas));

        comboLecciones.setConverter(new StringConverter<>() {
            @Override
            public String toString(Leccion l) {
                return l == null ? null : l.getTitulo();
            }

            @Override
            public Leccion fromString(String s) {
                return null;
            }
        });

        if (leccionesFiltradas.isEmpty()) {
            comboLecciones.setPromptText("No hay lecciones para este nivel");
        }
    }

        @FXML
    void handleAgregarRecurso(ActionEvent event) {
        String tipo = comboTipoRecurso.getValue();
        String titulo = txtTituloRecurso.getText();
        String url = txtUrlRecurso.getText();
        String datoExtra = txtDatoExtraRecurso.getText();

        if (tipo == null || titulo.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Debe seleccionar un tipo y escribir un título.").show();
            return;
        }

        RecursoAprendizaje nuevoRecurso = null;
        try {
            switch (tipo) {
                case "Video": nuevoRecurso = new Video(titulo, url, Integer.parseInt(datoExtra)); break;
                case "PDF": nuevoRecurso = new DocumentoPDF(titulo, url, Integer.parseInt(datoExtra)); break;
                case "Artículo": nuevoRecurso = new Articulo(titulo, url); break;
            }
            if (nuevoRecurso != null) {
                recursosTemporales.add(nuevoRecurso);
                limpiarCamposRecurso();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El dato extra debe ser un número para Videos y PDFs.").show();
        }
    }

    @FXML
    void handleGuardarNodo(ActionEvent event) {
        Leccion leccionSeleccionada = comboLecciones.getSelectionModel().getSelectedItem();
        if (leccionSeleccionada == null) {
            new Alert(Alert.AlertType.ERROR, "Debe seleccionar una lección para el nodo.").show();
            return;
        }

        int nuevoOrden = rutaActual.getNodos().size() + 1;
        NodoRuta nuevoNodo = new NodoRuta(nuevoOrden, leccionSeleccionada);

        // Añade los recursos que el admin preparó en la lista temporal
        for (RecursoAprendizaje recurso : recursosTemporales) {
            nuevoNodo.agregarMaterialDeApoyo(recurso);
        }

        // Modifica el objeto Ruta (que es una referencia al del repositorio)
        rutaActual.agregarNodo(nuevoNodo);

        cerrarVentana();
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void limpiarCamposRecurso() {
        comboTipoRecurso.getSelectionModel().clearSelection();
        txtTituloRecurso.clear();
        txtUrlRecurso.clear();
        txtDatoExtraRecurso.clear();
    }

    private void cerrarVentana() {
        ((Stage) btnGuardar.getScene().getWindow()).close();
    }
}