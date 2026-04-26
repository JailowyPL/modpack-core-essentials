package com.moonplay.modpackcore;

import com.moonplay.modpackcore.client.WelcomeMessageHandler;
import com.moonplay.modpackcore.client.WindowCustomizer;
import com.moonplay.modpackcore.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.lifecycle.v1.ClientLifecycleEvents;

public class ModpackCoreEssentialsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModConfig config = ModConfig.load();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            WindowCustomizer.apply(config);
        });

        WelcomeMessageHandler.register(config);
    }
}
