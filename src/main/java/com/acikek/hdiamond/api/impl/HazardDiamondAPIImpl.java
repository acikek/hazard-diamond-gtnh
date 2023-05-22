package com.acikek.hdiamond.api.impl;

import com.acikek.hdiamond.client.screen.HazardScreen;
import net.minecraft.client.Minecraft;

public class HazardDiamondAPIImpl {

    public static void setScreen(HazardScreen screen) {
        Minecraft.getMinecraft().currentScreen = screen;
    }
}
