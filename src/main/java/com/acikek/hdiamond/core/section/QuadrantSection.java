package com.acikek.hdiamond.core.section;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public interface QuadrantSection<E extends Enum<E>> extends DiamondSection<E> {

    EnumChatFormatting getTypeColor();

    default IChatComponent getTypeName() {
        return getText("type").setChatStyle(new ChatStyle().setColor(getTypeColor()));
    }

    @Override
    default IChatComponent getTitle() {
        return getTypeName()
                .appendSibling(new ChatComponentText(EnumChatFormatting.GRAY + " - "))
                .appendSibling(DiamondSection.super.getTitle().setChatStyle(new ChatStyle().setColor(getLevelColor())));
    }

    default IChatComponent getSymbol() {
        return new ChatComponentText(getTypeColor() + String.valueOf(getValue().ordinal()));
    }

    default EnumChatFormatting getLevelColor() {
        switch (getValue().ordinal()) {
            case 0:
            case 1:
                return EnumChatFormatting.GREEN;
            case 2:
            case 3:
                return EnumChatFormatting.YELLOW;
            case 4:
                return EnumChatFormatting.RED;
            default:
                return EnumChatFormatting.WHITE;
        }
    }
}
