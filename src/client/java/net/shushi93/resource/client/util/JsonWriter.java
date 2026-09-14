package net.shushi93.resource.client.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.shushi93.resource.ResourceScreening;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class JsonWriter {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path config_path = FabricLoader.getInstance().getConfigDir().resolve("resource-screening").resolve("client.json");

    public static void write_json(Map<?, ?> m) {
        try {
            Files.createDirectories(config_path.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.writeString(config_path, gson.toJson(m));
        } catch (Exception e) {
            LoggerFactory.getLogger(ResourceScreening.MOD_ID).error("Failed to write JSON: {}", e.getMessage());
        }
    }

    public static Map<String, String> get_map() {
        try {
            if (!Files.exists(config_path)) {
                return new HashMap<>();
            }
            String json = Files.readString(config_path);
            return gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (Exception e) {
            LoggerFactory.getLogger(ResourceScreening.MOD_ID).error("Failed to read JSON: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}
