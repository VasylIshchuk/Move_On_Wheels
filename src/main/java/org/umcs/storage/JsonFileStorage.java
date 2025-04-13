package org.umcs.storage;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonFileStorage<T> {
    private final String filePath;

    public JsonFileStorage(String filePath) {
        this.filePath = filePath;
    }

    public void saveToFile(List<T> objects) {
        try (BufferedWriter writer = getWriter()) {
            writer.write("[\n");

            for (int i = 0; i < objects.size(); i++) {
                saveObject(writer, objects.get(i));

                if (i < objects.size() - 1) {
                    writer.write(",\n");
                }
            }

            writer.write("\n]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedWriter getWriter() {
        try {
            return new BufferedWriter(new FileWriter(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveObject(BufferedWriter writer, T object) throws IOException {
        if (object instanceof JsonSerializable) {
            writer.write(((JsonSerializable) object).toJSON());
        } else {
            throw new IllegalArgumentException("Object must implement JsonSerializable");
        }
    }

    public List<T> load(Type type) {
        Gson gson = new Gson();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return new ArrayList<>();
        try {
            String json = Files.readString(path);
            List<T> list = gson.fromJson(json, type);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
