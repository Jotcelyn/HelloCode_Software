package GestionAprendizaje_Modulo.Servicio;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import GestionAprendizaje_Modulo.Modelo.RecursoAprendizaje;

import java.io.IOException;

/**
 * Este TypeAdapter personalizado le enseña a Gson cómo manejar la jerarquía de clases
 * que heredan de RecursoAprendizaje.
 */
public class RecursoAprendizajeAdapter extends TypeAdapter<RecursoAprendizaje> {

    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public void write(JsonWriter out, RecursoAprendizaje value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        // 1. Convertimos el objeto concreto (ej. Video) a un árbol JSON
        Gson gson = new Gson();
        JsonElement tree = gson.toJsonTree(value);

        // 2. Creamos un nuevo objeto JSON que contendrá los datos y el tipo
        JsonObject customObject = new JsonObject();
        customObject.add(CLASSNAME, new JsonPrimitive(value.getClass().getName()));
        customObject.add(INSTANCE, tree);

        // 3. Escribimos este objeto personalizado en el archivo
        gson.toJson(customObject, out);
    }

    @Override
    public RecursoAprendizaje read(JsonReader in) throws IOException {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(in, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // 1. Leemos el nombre de la clase que guardamos
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();

        // 2. Usamos reflexión para obtener la clase concreta
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }

        // 3. Le decimos a Gson que use esa clase concreta para leer el resto de los datos
        return gson.fromJson(jsonObject.get(INSTANCE), (Class<? extends RecursoAprendizaje>) clazz);
    }
}