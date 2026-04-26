package com.moonplay.modpackcore;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModpackCoreEssentials implements ModInitializer {
    public static final String MOD_ID = "modpack_core_essentials";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[ModpackCoreEssentials] Initialized.");
    }
}
