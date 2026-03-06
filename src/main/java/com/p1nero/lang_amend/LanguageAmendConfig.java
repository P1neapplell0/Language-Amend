package com.p1nero.lang_amend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LanguageAmendConfig {
    private static final Logger LOGGER = LogManager.getLogger("LanguageAmend");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, List<String>>>() {}.getType();

    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("lang_amend/lang_amend.json");
    private static Map<String, List<String>> config = null;

    public static Map<String, List<String>> getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    protected static void loadConfig() {
        Path dir = CONFIG_PATH.getParent();
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                LOGGER.error("无法创建配置目录: {}", dir, e);
            }
        }

        if (!Files.exists(CONFIG_PATH)) {
            Map<String, List<String>> defaultConfig = new LinkedHashMap<>();
            defaultConfig.put("lzh", List.of("zh_cn"));
            saveConfig(defaultConfig);
            config = defaultConfig;
        } else {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                config = GSON.fromJson(reader, MAP_TYPE);
                if (config == null) {
                    config = new LinkedHashMap<>();
                }
            } catch (IOException e) {
                LOGGER.error("读取配置文件失败，将使用空配置", e);
                config = new LinkedHashMap<>();
            }
        }
    }

    public static void saveConfig(Map<String, List<String>> newConfig) {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(newConfig, writer);
        } catch (IOException e) {
            LOGGER.error("保存配置文件失败", e);
        }
    }

}