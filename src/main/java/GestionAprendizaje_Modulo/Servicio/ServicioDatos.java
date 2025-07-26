package GestionAprendizaje_Modulo.Servicio;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Modelo.Leccion;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje; // Importante
import GestionAprendizaje_Modulo.Ruta.Ruta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServicioDatos {

    private static final String SAVE_FILE = "datos_app.json";
    private static final ServicioDatos INSTANCIA = new ServicioDatos();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RecursoAprendizaje.class, new RecursoAprendizajeAdapter())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private DatosGuardados datos;

    private ServicioDatos() {
        cargarDatos();
    }

    public static ServicioDatos getInstancia() {
        return INSTANCIA;
    }

    public Map<String, Curso> getCursos() { return datos.getCursos(); }
    public List<Leccion> getLecciones() { return datos.getLecciones(); }

    public synchronized void save() {
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            gson.toJson(datos, writer);
            System.out.println(">>> Datos guardados exitosamente en " + SAVE_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        try (FileReader reader = new FileReader(SAVE_FILE)) {
            Type tipoDatos = new TypeToken<DatosGuardados>(){}.getType();
            datos = gson.fromJson(reader, tipoDatos);

            // --- LÓGICA MEJORADA ---
            // Si el archivo se cargó pero no hay lecciones (estado corrupto), reiniciamos.
            if (datos == null || datos.getCursos() == null || datos.getLecciones() == null || datos.getLecciones().isEmpty()) {
                System.out.println("Archivo de datos vacío o corrupto. Reiniciando con datos de ejemplo.");
                inicializarYPrecargar();
            } else {
                System.out.println(">>> Datos cargados exitosamente desde " + SAVE_FILE);
            }
        } catch (IOException e) {
            // Si el archivo no existe, también reiniciamos.
            inicializarYPrecargar();
        }
    }

    private void inicializarYPrecargar() {
        System.out.println("Creando un nuevo archivo de datos con valores por defecto...");
        datos = new DatosGuardados();
        precargarLeccionesDeEjemplo();
        precargarCursoYRutaDeEjemplo();
        save();
    }

    private void precargarLeccionesDeEjemplo() {
        datos.getLecciones().clear();
        datos.getLecciones().add(new Leccion(UUID.randomUUID().toString(), "Variables en Java", "Aprende los tipos de datos primitivos.", "PUBLICADO", "Java", "Principiante"));
        datos.getLecciones().add(new Leccion(UUID.randomUUID().toString(), "Bucles 'for' en Java", "Domina la repetición de tareas.", "PUBLICADO", "Java", "Principiante"));
        datos.getLecciones().add(new Leccion(UUID.randomUUID().toString(), "Clases y Objetos", "El pilar de la POO.", "PUBLICADO", "Java", "Intermedio"));
        datos.getLecciones().add(new Leccion(UUID.randomUUID().toString(), "Herencia y Polimorfismo", "Conceptos avanzados de POO.", "PUBLICADO", "Java", "Avanzado"));
        datos.getLecciones().add(new Leccion(UUID.randomUUID().toString(), "Variables y Listas", "Fundamentos de Python.", "PUBLICADO", "Python", "Principiante"));
    }

    private void precargarCursoYRutaDeEjemplo() {
        // Creamos un curso de ejemplo para Java
        Curso cursoJava = new Curso("Java", "Curso completo de Java desde cero.");
        Ruta rutaJavaPrincipiante = new Ruta(UUID.randomUUID().toString(), "Principiante", "Conceptos básicos para empezar.");
        cursoJava.getRutas().add(rutaJavaPrincipiante);
        datos.getCursos().put(cursoJava.getId(), cursoJava);

        // Creamos un curso de ejemplo para Python
        Curso cursoPython = new Curso("Python", "Curso completo de Python desde cero.");
        Ruta rutaPythonPrincipiante = new Ruta(UUID.randomUUID().toString(), "Principiante", "Conceptos básicos de Python.");
        cursoPython.getRutas().add(rutaPythonPrincipiante);
        datos.getCursos().put(cursoPython.getId(), cursoPython);
    }
}

class DatosGuardados {
    private Map<String, Curso> cursos = new ConcurrentHashMap<>();
    private List<Leccion> lecciones = new ArrayList<>();
    public Map<String, Curso> getCursos() { return cursos; }
    public List<Leccion> getLecciones() { return lecciones; }
}