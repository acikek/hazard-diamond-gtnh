package com.acikek.hdiamond;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class HDiamondConfig {

    public static boolean renderFull = true;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        renderFull = configuration.getBoolean(
            "renderFull", Configuration.CATEGORY_GENERAL, renderFull,
            "Whether to render Hazard Data icons in-game"
        );
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
