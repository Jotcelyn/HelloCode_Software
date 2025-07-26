package GestionAprendizaje_Modulo.Repositorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import GestionAprendizaje_Modulo.Modelo.Curso;

public class CursoRepository {
    private static final String RUTA_BASE = "src/main/resources/GestionAprendizaje_Modulo/data/";
    private static final String ARCHIVO = "DB_Cursos.txt";

    /** Carga todos los cursos desde el archivo. */
    public static List<Curso> cargarCursos() {
    List<Curso> lista = new ArrayList<>();
    Path path = Paths.get(RUTA_BASE + ARCHIVO);
    if (!Files.exists(path)) return lista;

    try (BufferedReader br = Files.newBufferedReader(path)) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.isBlank()) continue;
            String[] partes = linea.split("\\|", 3);
            if (partes.length == 3) {
                // Usa el constructor nuevo que preserva el id
                Curso c = new Curso(partes[0], partes[1], partes[2]);
                lista.add(c);
            }
        }
    } catch (IOException e) {
        System.err.println("Error al leer cursos: " + e.getMessage());
    }
    return lista;
}

    /** Guarda (añade) un curso al archivo. */
    public static void guardarCurso(Curso curso) {
        Path path = Paths.get(RUTA_BASE + ARCHIVO);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(path,
                      StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                String linea = String.join("|",
                    curso.getId(), curso.getNombre(), curso.getDescripcion());
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar curso: " + e.getMessage());
        }
    }
}
