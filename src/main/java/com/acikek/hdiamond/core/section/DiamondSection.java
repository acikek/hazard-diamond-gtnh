package com.acikek.hdiamond.core.section;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public interface DiamondSection<E extends Enum<E>> {

    class Texture {

        int u;
        int v;
        int width;
        int height;

        public Texture(int u, int v, int width, int height) {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }

        public static Texture numeral(int row, int index) {
            return new Texture(64 + index * 12, 14 * row, 12, 14);
        }
        public int u() {
            return u;
        }

        public int v() {
            return v;
        }

        public int width() {
            return width;
        }

        public int height() {
            return height;
        }
    }

    E getValue();

    Texture getTexture();

    String getType();

    default IChatComponent getText(String suffix) {
        return new ChatComponentTranslation(getType() + "." + suffix);
    }

    default IChatComponent getSpecificText(String suffix) {
        return getText(getValue().name().toLowerCase() + "." + suffix);
    }

    default IChatComponent getTitle() {
        return getSpecificText("name");
    }

    default IChatComponent getDescription() {
        return getSpecificText("description");
    }
}
