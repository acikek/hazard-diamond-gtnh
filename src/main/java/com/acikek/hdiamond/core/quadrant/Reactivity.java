package com.acikek.hdiamond.core.quadrant;

import com.acikek.hdiamond.core.section.QuadrantSection;
import net.minecraft.util.EnumChatFormatting;

/**
 * Describes the susceptibiltiy of a material to release energy.<br>
 * Source: NFPA, <em>Fire Protection on Hazardous Materials</em>, Standard 704, 1990
 */
public enum Reactivity implements QuadrantSection<Reactivity> {
    /**
     * Normally stable, even under fire exposure conditions, and not reactive with water.
     */
    STABLE,
    /**
     * Normally stable, but can become unstable at elevated temperatures and pressures.
     */
    SENSITIVE,
    /**
     * Readily undergoes violent chemical change at elevated temperatures and pressures,
     * or reacts violently or forms explosive mixtures with water.
     */
    VIOLENT,
    /**
     * Reacts explosively with water, or is capable of detonation or explosive decomposition or reaction
     * but requires a strong initiating source or must be heated under confinement before initiation.
     */
    MAY_DETONATE,
    /**
     * Readily capable of detonation or explosive decomposition or reaction at normal temperatures and pressures.
     */
    READILY_DETONATES;

    private final Texture texture = Texture.numeral(2, ordinal());

    @Override
    public Reactivity getValue() {
        return this;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getType() {
        return "quadrant.hdiamond.reactivity";
    }

    @Override
    public EnumChatFormatting getTypeColor() {
        return EnumChatFormatting.YELLOW;
    }
}
