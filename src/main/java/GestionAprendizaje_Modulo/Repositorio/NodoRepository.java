package GestionAprendizaje_Modulo.Repositorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import GestionAprendizaje_Modulo.Modelo.Articulo;
import GestionAprendizaje_Modulo.Modelo.DocumentoPDF;
import GestorEjercicios.model.Leccion;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;
import GestionAprendizaje_Modulo.Modelo.Video;
import GestionAprendizaje_Modulo.Ruta.NodoRuta;
import GestionAprendizaje_Modulo.Ruta.Ruta;

public class NodoRepository {
    private static final String RUTA_BASE = "src/main/resources/GestionAprendizaje_Modulo/data/";
    private static final String ARCHIVO = "DB_Nodos.txt";

    // Guarda un nodo en el archivo
    public static void guardarNodo(NodoRuta nodo, Ruta ruta) {
        Path path = Paths.get(RUTA_BASE + ARCHIVO);
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                StringBuilder recursos = new StringBuilder();
                for (RecursoAprendizaje r : nodo.getMaterialDeApoyo()) {
                    if (recursos.length() > 0) recursos.append(";");
                    if (r instanceof Video) {
                        Video v = (Video) r;
                        recursos.append("Video,").append(v.getTitulo()).append(",").append(v.getUrl()).append(",").append(v.getDuracionSegundos());
                    } else if (r instanceof DocumentoPDF) {
                        DocumentoPDF pdf = (DocumentoPDF) r;
                        recursos.append("PDF,").append(pdf.getTitulo()).append(",").append(pdf.getUrlDescarga()).append(",").append(pdf.getNumPaginas());
                    } else if (r instanceof Articulo) {
                        Articulo a = (Articulo) r;
                        recursos.append("Articulo,").append(a.getTitulo()).append(",").append(a.getUrl()).append(",");
                    }
                }
                String linea = String.join("|",
                        ruta.getId(),
                        String.valueOf(nodo.getOrden()),
                        String.valueOf(nodo.getLeccion().getId()),
                        recursos.toString());
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar nodo: " + e.getMessage());
        }
    }

    // Carga todos los nodos y los asocia a las rutas
    public static void cargarNodos(List<Ruta> rutas, Map<String, Leccion> lecciones) {
        Path path = Paths.get(RUTA_BASE + ARCHIVO);
        if (!Files.exists(path)) return;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] p = linea.split("\\|", 4);
                if (p.length >= 4) {
                    String rutaId = p[0];
                    int orden = Integer.parseInt(p[1]);
                    String leccionId = p[2];
                    String recursosStr = p[3];

                    Ruta ruta = rutas.stream().filter(r -> r.getId().equals(rutaId)).findFirst().orElse(null);
                    Leccion leccion = lecciones.get(leccionId);
                    if (ruta != null && leccion != null) {
                        NodoRuta nodo = new NodoRuta(orden, leccion);
                        if (!recursosStr.isBlank()) {
                            String[] recursosArr = recursosStr.split(";");
                            for (String rec : recursosArr) {
                                String[] datos = rec.split(",", 4);
                                if (datos.length >= 3) {
                                    switch (datos[0]) {
                                        case "Video":
                                            nodo.agregarMaterialDeApoyo(new Video(datos[1], datos[2], Integer.parseInt(datos[3])));
                                            break;
                                        case "PDF":
                                            nodo.agregarMaterialDeApoyo(new DocumentoPDF(datos[1], datos[2], Integer.parseInt(datos[3])));
                                            break;
                                        case "Articulo":
                                            nodo.agregarMaterialDeApoyo(new Articulo(datos[1], datos[2]));
                                            break;
                                    }
                                }
                            }
                        }
                        ruta.agregarNodo(nodo);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer nodos: " + e.getMessage());
        }
    }
}