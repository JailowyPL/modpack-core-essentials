package com.moonplay.modpackcore.client;

import com.moonplay.modpackcore.ModpackCoreEssentials;
import com.moonplay.modpackcore.config.ModConfig;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class WelcomeMessageHandler {
    private WelcomeMessageHandler() {}

    private static boolean hasShown = false;
    private static final int DURATION = 300; // ticks (~15s)
    private static final int FADE     = 60;  // ticks (~3s)

    public static void register(ModConfig config) {
        if (!config.showWelcomeOnStartup) return;

        ScreenEvents.AFTER_INIT.register((client, screen, w, h) -> {
            if (!(screen instanceof TitleScreen) || hasShown) return;
            hasShown = true;

            final int[] ticks = { DURATION };

            ScreenEvents.afterRender(screen).register((s, graphics, mouseX, mouseY, delta) -> {
                if (ticks[0] <= 0) return;
                ticks[0]--;
                float alpha = ticks[0] > FADE ? 1f : (float) ticks[0] / FADE;
                render(client, graphics, config.welcomeMessage, alpha);
            });
        });
    }

    private static void render(Minecraft mc, GuiGraphics graphics, List<String> lines, float alpha) {
        if (lines == null || lines.isEmpty()) return;

        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();
        int lh = mc.font.lineHeight + 3;
        int totalH = lines.size() * lh;
        int maxW = lines.stream()
            .mapToInt(l -> mc.font.width(Component.literal(l)))
            .max().orElse(80);

        int pad  = 8;
        int bx   = sw / 2 - maxW / 2;
        int by   = sh - totalH - 40;
        int bgA  = (int)(alpha * 0x99) << 24;

        graphics.fill(bx - pad, by - pad, bx + maxW + pad, by + totalH + pad, bgA);

        int textA = (int)(alpha * 0xFF) << 24;
        for (int i = 0; i < lines.size(); i++) {
            Component text = Component.literal(lines.get(i));
            int x = sw / 2 - mc.font.width(text) / 2;
            int y = by + i * lh;
            graphics.drawString(mc.font, text, x, y, textA | 0xFFFFFF);
        }
    }
}
