package com.acikek.hdiamond.core.quadrant;

import com.acikek.hdiamond.core.section.QuadrantSection;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

/**
 * Describes special hazards pertaining to a material.
 * Source: NFPA, <em>Fire Protection on Hazardous Materials</em>, Standard 704, 1990
 */
public enum SpecificHazard implements QuadrantSection<SpecificHazard> {
    /**
     * No specific hazard to denote.
     */
    NONE,
    /**
     * Reacts with water in an unusual or dangerous manner.
     */
    REACTS_WITH_WATER,
    /**
     * Allows chemicals to burn without air supply.
     */
    OXIDIZER,
    /**
     * Denotes a "simple asphyxiant gas," or one that alters the normal oxygen concentration in breathable air.
     */
    SIMPLE_ASPHYXIANT,
    /**
     * Decays radioactively.
     */
    RADIOACTIVE;

    private Texture texture = null;

    @Override
    public SpecificHazard getValue() {
        return this;
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            texture = this == SpecificHazard.RADIOACTIVE
                    ? new Texture(256 - 20, 256, 20, 20)
                    : new Texture(64 + (ordinal() - 1) * 18, 42, 18, 14);
        }
        return texture;
    }

    @Override
    public String getType() {
        return "quadrant.hdiamond.specific";
    }

    @Override
    public EnumChatFormatting getTypeColor() {
        return EnumChatFormatting.WHITE;
    }

    @Override
    public IChatComponent getSymbol() {
        var text = switch (this) {
            case NONE -> new ChatComponentText("");
            case REACTS_WITH_WATER -> new ChatComponentText(EnumChatFormatting.STRIKETHROUGH + "W");
            case OXIDIZER -> new ChatComponentText("OX");
            case SIMPLE_ASPHYXIANT -> new ChatComponentText("SA");
            case RADIOACTIVE -> new ChatComponentText("R");
        };
        return text.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
    }

    @Override
    public EnumChatFormatting getLevelColor() {
        return switch (this) {
            case NONE -> EnumChatFormatting.GRAY;
            case REACTS_WITH_WATER -> EnumChatFormatting.DARK_AQUA;
            case OXIDIZER -> EnumChatFormatting.RED;
            case SIMPLE_ASPHYXIANT -> EnumChatFormatting.GOLD;
            case RADIOACTIVE -> EnumChatFormatting.DARK_GREEN;
        };
    }
}
