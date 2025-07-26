package GestionAprendizaje_Modulo.Repositorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GestionAprendizaje_Modulo.Modelo.Curso;
import GestionAprendizaje_Modulo.Ruta.Ruta;

public class RutaRepository {
    private static final String RUTA_BASE = "src/main/resources/GestionAprendizaje_Modulo/data/";
    private static final String ARCHIVO = "DB_Rutas.txt";

    /** Carga todas las rutas y las asocia a sus cursos. */
    public static List<Ruta> cargarRutas(List<Curso> cursos) {
        Map<String, Curso> mapa = new HashMap<>();
        for (Curso c : cursos) mapa.put(c.getId(), c);

        List<Ruta> rutas = new ArrayList<>();
        Path path = Paths.get(RUTA_BASE + ARCHIVO);
        if (!Files.exists(path)) return rutas;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] p = linea.split("\\|", 4);
                if (p.length == 4) {
                    String rutaId = p[0], cursoId = p[1], nombre = p[2], desc = p[3];
                    Ruta r = new Ruta(rutaId, nombre, desc, cursoId);
                    Curso curso = mapa.get(cursoId);
                    if (curso != null) {
                        curso.getRutas().add(r);
                        rutas.add(r);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer rutas: " + e.getMessage());
        }
        return rutas;
    }

    /** Guarda (añade) una nueva ruta al archivo. */
    public static void guardarRuta(Ruta ruta, Curso curso) {
        Path path = Paths.get(RUTA_BASE + ARCHIVO);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(path,
                      StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                String linea = String.join("|",
                    ruta.getId(), curso.getId(), ruta.getNombre(), ruta.getDescripcion());
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar ruta: " + e.getMessage());
        }
    }
}
