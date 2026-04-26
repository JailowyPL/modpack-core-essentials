package com.moonplay.modpackcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class ModConfig {
    public String windowTitle = "Minecraft \u2014 MoonPlay Modpack";
    public boolean useCustomIcon = false;
    public String iconPath = "config/icon.png";
    public List<String> welcomeMessage = Arrays.asList(
        "\u00a76\u2726 Welcome to MoonPlay Modpack! \u2726",
        "\u00a77Enjoy your adventure and have fun.",
        "\u00a78(This message shows only once per launch)"
    );
    public boolean showWelcomeOnStartup = true;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("custom_window.json");
    }

    public static ModConfig load() {
        Path path = configPath();
        if (Files.exists(path)) {
            try (Reader r = Files.newBufferedReader(path)) {
                ModConfig cfg = GSON.fromJson(r, ModConfig.class);
                if (cfg != null) return cfg;
            } catch (IOException e) {
                System.err.println("[MCE] Failed to read config: " + e.getMessage());
            }
        }
        ModConfig def = new ModConfig();
        def.save();
        return def;
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(configPath())) {
            GSON.toJson(this, w);
        } catch (IOException e) {
            System.err.println("[MCE] Failed to save config: " + e.getMessage());
        }
    }
}
