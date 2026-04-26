package com.moonplay.modpackcore.client;

import com.moonplay.modpackcore.ModpackCoreEssentials;
import com.moonplay.modpackcore.config.ModConfig;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class WindowCustomizer {
    private WindowCustomizer() {}

    public static void apply(ModConfig config) {
        Minecraft mc = Minecraft.getInstance();

        if (config.windowTitle != null && !config.windowTitle.isBlank()) {
            try {
                mc.getWindow().setTitle(config.windowTitle);
                ModpackCoreEssentials.LOGGER.info("[MCE] Window title set to: {}", config.windowTitle);
            } catch (Exception e) {
                ModpackCoreEssentials.LOGGER.error("[MCE] Failed to set window title", e);
            }
        }

        if (config.useCustomIcon) {
            applyIcon(mc, config.iconPath);
        }
    }

    private static void applyIcon(Minecraft mc, String iconPath) {
        if (iconPath == null || iconPath.isBlank()) return;

        File file = mc.gameDirectory.toPath().resolve(iconPath).toFile();
        if (!file.exists()) {
            ModpackCoreEssentials.LOGGER.warn("[MCE] Icon not found at: {}", file.getAbsolutePath());
            return;
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);
            ByteBuffer pixels = STBImage.stbi_load(file.getAbsolutePath(), w, h, c, 4);
            if (pixels == null) {
                ModpackCoreEssentials.LOGGER.error("[MCE] STBImage failed: {}", STBImage.stbi_failure_reason());
                return;
            }
            GLFWImage.Buffer icons = GLFWImage.malloc(1, stack);
            icons.position(0).width(w.get(0)).height(h.get(0)).pixels(pixels);
            GLFW.glfwSetWindowIcon(mc.getWindow().getWindow(), icons);
            STBImage.stbi_image_free(pixels);
            ModpackCoreEssentials.LOGGER.info("[MCE] Icon applied ({}x{})", w.get(0), h.get(0));
        } catch (Exception e) {
            ModpackCoreEssentials.LOGGER.error("[MCE] Failed to apply icon", e);
        }
    }
}
