// package GestionAprendizaje_Modulo.Controladores;

// import GestionAprendizaje_Modulo.Modelo.Curso;
// import GestionAprendizaje_Modulo.Ruta.NodoRuta;
// import GestionAprendizaje_Modulo.Ruta.Ruta;
// import GestionAprendizaje_Modulo.Servicio.DatosManager;
// import GestorEjercicios.model.Leccion;
// import MetodosGlobales.MetodosFrecuentes;
// import Modulo_Ejercicios.Controladores.EjercicioSeleccionController;
// import javafx.collections.FXCollections;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.geometry.Pos;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ComboBox;
// import javafx.scene.control.Label;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.VBox;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Circle;
// import javafx.scene.text.Font;
// import javafx.scene.text.Text;
// import javafx.stage.Modality;
// import javafx.stage.Stage;
// import javafx.util.StringConverter;

// import java.io.IOException;
// import java.util.List;

// public class StudentDashboardController {

//     private final DatosManager datosManager = DatosManager.getInstancia();

//     @FXML private ComboBox<Curso> comboCursos;
//     @FXML private ComboBox<Ruta> comboRutas;
//     @FXML private Label labelNombreRuta;
//     @FXML private VBox pathContainerEstudiante;
//     @FXML private Button buttonAtrasE;

//     // Nuevos campos para almacenar el curso inicial y el filtro de nivel
//     private Curso initialCourse;
//     private String initialLevelFilter; // Ej: "Principiante", "Intermedio", "Avanzado"

//     /**
//      * Setter para recibir el curso seleccionado y el filtro de nivel.
//      * Se llama antes de que se inicialice el FXML.
//      * @param course El curso seleccionado desde la vista anterior.
//      * @param levelFilter El nivel de ruta seleccionado (Principiante, Intermedio, Avanzado).
//      */
//     public void setInitialData(Curso course, String levelFilter) {
//         this.initialCourse = course;
//         this.initialLevelFilter = levelFilter;
//     }

//     @FXML
//     public void initialize() {
//         configurarComboBoxes();
//         // Carga los cursos desde el manager, que ya los leyó de los archivos
//         comboCursos.setItems(FXCollections.observableArrayList(datosManager.getCursos()));

//         // Aplicar datos iniciales si están disponibles (esto se ejecuta después de que los elementos FXML son inyectados)
//         if (initialCourse != null && initialLevelFilter != null) {
//             // Selecciona el curso inicial en el combo box
//             comboCursos.getSelectionModel().select(initialCourse);
//             // Ejecuta la lógica de selección del curso para poblar las rutas y seleccionar la correcta
//             onCourseSelectedForInitialDisplay();
//         } else {
//             // Si no hay datos iniciales, limpia el combo box de rutas y el contenedor de la ruta.
//             // Esto podría ocurrir si StudentDashboard.fxml se carga directamente sin datos iniciales.
//             comboRutas.getItems().clear();
//             pathContainerEstudiante.getChildren().clear();
//             labelNombreRuta.setText("Selecciona una ruta para ver el camino");
//         }
//     }

//     private void configurarComboBoxes() {
//         comboCursos.setConverter(new StringConverter<>() {
//             @Override public String toString(Curso c) { return c == null ? "" : c.getNombre(); }
//             @Override public Curso fromString(String s) { return null; }
//         });
//         comboRutas.setConverter(new StringConverter<>() {
//             @Override public String toString(Ruta r) { return r == null ? "" : r.getNombre(); }
//             @Override public Ruta fromString(String s) { return null; }
//         });
//     }

//     /**
//      * Método para manejar la lógica de selección del curso inicial sin intervención del usuario.
//      * Busca y renderiza la ruta que coincide con el nivel de filtro inicial.
//      */
//     private void onCourseSelectedForInitialDisplay() {
//         if (initialCourse != null) {
//             comboRutas.getItems().clear();
//             pathContainerEstudiante.getChildren().clear();
//             labelNombreRuta.setText("Selecciona una ruta para ver el camino");

//             List<Ruta> allRoutesForCourse = initialCourse.getRutas();
//             Ruta targetRuta = null;

//             // Encontrar la ruta que coincide con el filtro de nivel inicial
//             // Se asume que el nombre de la Ruta contiene la información del nivel
//             // (ej. "Ruta Principiante", "Ruta Intermedia", "Ruta Avanzada")
//             for (Ruta ruta : allRoutesForCourse) {
//                 if (ruta.getNombre().toLowerCase().contains(initialLevelFilter.toLowerCase())) {
//                     targetRuta = ruta;
//                     break;
//                 }
//             }

//             if (targetRuta != null) {
//                 comboRutas.getSelectionModel().select(targetRuta); // Seleccionarla en el combo box
//                 renderizarRutaEstudiante(targetRuta); // Renderizarla directamente
//             } else {
//                 labelNombreRuta.setText("No se encontró una ruta para el nivel '" + initialLevelFilter + "' en el curso '" + initialCourse.getNombre() + "'.");
//                 showAlert("Ruta no encontrada", "No se pudo encontrar una ruta que coincida con el nivel seleccionado (" + initialLevelFilter + ") en el curso " + initialCourse.getNombre() + ".");
//             }
//             // Poblar el comboRutas con todas las rutas del curso seleccionado, incluso si una fue pre-seleccionada
//             comboRutas.setItems(FXCollections.observableArrayList(allRoutesForCourse));
//         }
//     }


//     @FXML
//     void onCursoSeleccionado(ActionEvent event) {
//         // Este método se llamará si el usuario cambia manualmente el curso en el combo box
//         // Debe reiniciar la selección de la ruta y la visualización de la ruta.
//         Curso curso = comboCursos.getSelectionModel().getSelectedItem();
//         comboRutas.getItems().clear();
//         pathContainerEstudiante.getChildren().clear();
//         labelNombreRuta.setText("Selecciona una ruta para ver el camino");
//         if (curso != null) {
//             comboRutas.setItems(FXCollections.observableArrayList(curso.getRutas()));
//         }
//     }

//     @FXML
//     void onRutaSeleccionada(ActionEvent event) {
//         // Este método se llamará si el usuario selecciona manualmente una ruta del combo box
//         Ruta ruta = comboRutas.getSelectionModel().getSelectedItem();
//         renderizarRutaEstudiante(ruta);
//     }

//     /**
//      * Dibuja el camino visual de la ruta en la interfaz del estudiante.
//      * @param ruta La ruta a dibujar.
//      */
//     private void renderizarRutaEstudiante(Ruta ruta) {
//         pathContainerEstudiante.getChildren().clear();
//         if (ruta == null) {
//             labelNombreRuta.setText("Selecciona una ruta para ver el camino");
//             return;
//         }

//         labelNombreRuta.setText("Tu Camino: " + ruta.getNombre());
//         for (NodoRuta nodo : ruta.getNodos()) {
//             // Verificar si nodo.getLeccion() es nulo antes de continuar para evitar NullPointerException
//             if (nodo.getLeccion() == null) {
//                 System.err.println("Advertencia: NodoRuta con orden " + nodo.getOrden() + " no tiene lección asociada.");
//                 continue; // Saltar este nodo si no tiene una lección asociada
//             }
//             // Creamos el componente visual para cada nodo
//             HBox nodoVisual = crearComponenteVisualNodo(nodo);

//             // ¡LA MAGIA! Añadimos un evento de clic a cada componente del nodo.
//             nodoVisual.setOnMouseClicked(mouseEvent -> {
//                 handleNodoClick(nodo);
//             });

//             pathContainerEstudiante.getChildren().add(nodoVisual);
//         }
//     }

//     /**
//      * Lógica de integración clave. Se ejecuta cuando el estudiante hace clic en un nodo.
//      * Abre la vista de ejercicios del otro módulo, pasándole la lección correspondiente.
//      * @param nodo El nodo específico en el que se hizo clic.
//      */
//     private void handleNodoClick(NodoRuta nodo) {
//         if (nodo == null || nodo.getLeccion() == null) {
//             new Alert(Alert.AlertType.ERROR, "Error: Este nodo no tiene una lección asociada.").show();
//             return;
//         }

//         Leccion leccionSeleccionada = nodo.getLeccion();

//         // Marcamos el progreso y le decimos al manager que lo guarde en el archivo
//         nodo.marcarComoCompletado();
//         datosManager.guardarProgresoEstudiante(); // Esto es importante para la persistencia del progreso

//         // Redibujamos la vista principal para que el tick de completado aparezca inmediatamente
//         renderizarRutaEstudiante(comboRutas.getSelectionModel().getSelectedItem());

//         System.out.println("Estudiante seleccionó la lección: " + leccionSeleccionada.getNombre());
//         System.out.println("Intentando abrir la vista de ejercicios del otro módulo...");

//         try {
//             String fxmlPath = "/Modulo_Ejercicios/views/SeleccionMultiple-view.fxml";
//             FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//             Parent root = loader.load();

//             EjercicioSeleccionController controllerEjercicios = loader.getController();

//             // Aquí debes pasar la información necesaria al controlador del módulo de ejercicios.
//             // Por ejemplo: controllerEjercicios.setEjercicios(leccionSeleccionada.getEjercicios());

//             Stage stage = new Stage();
//             stage.setTitle("Lección: " + leccionSeleccionada.getNombre());
//             stage.initModality(Modality.WINDOW_MODAL);
//             stage.initOwner(pathContainerEstudiante.getScene().getWindow());
//             stage.setScene(new Scene(root));
//             stage.show();

//         } catch (Exception e) {
//             e.printStackTrace();
//             new Alert(Alert.AlertType.ERROR, "No se pudo cargar el módulo de ejercicios. Verifica la integración y la ruta del FXML.").show();
//         }
//     }

//     /**
//      * Crea el componente visual (HBox con círculo y texto) para un nodo.
//      * @param nodo El objeto NodoRuta a dibujar.
//      * @return Un HBox que representa visualmente el nodo.
//      */
//     private HBox crearComponenteVisualNodo(NodoRuta nodo) {
//         HBox nodoBox = new HBox(10);
//         nodoBox.setAlignment(Pos.CENTER_LEFT);
//         nodoBox.setStyle("-fx-background-color: #E8E8E8; -fx-padding: 10; -fx-background-radius: 10; -fx-cursor: hand;");

//         String color = nodo.estaCompletado() ? "#4CAF50" : "#2196F3";
//         Circle circulo = new Circle(15, Color.web(color));

//         Text textoOrden = new Text(nodo.estaCompletado() ? "✔" : String.valueOf(nodo.getOrden()));
//         textoOrden.setFont(Font.font("System", 12));
//         textoOrden.setFill(Color.WHITE);

//         javafx.scene.layout.StackPane stack = new javafx.scene.layout.StackPane(circulo, textoOrden);
//         Text textoLeccion = new Text(nodo.getLeccion().getNombre());
//         textoLeccion.setFont(Font.font("System", 14));

//         nodoBox.getChildren().addAll(stack, textoLeccion);
//         return nodoBox;
//     }

//     @FXML
//     private void regresarPrincipalE() {
//         MetodosFrecuentes.cambiarVentana((Stage) buttonAtrasE.getScene().getWindow(),"/GestionAprendizaje_Modulo/Vistas/selectorRol.fxml","ROL");
//     }

//     /**
//      * Método auxiliar para mostrar alertas.
//      */
//     private void showAlert(String title, String message) {
//         Alert alert = new Alert(Alert.AlertType.INFORMATION);
//         alert.setTitle(title);
//         alert.setHeaderText(null);
//         alert.setContentText(message);
//         alert.showAndWait();
//     }
// }