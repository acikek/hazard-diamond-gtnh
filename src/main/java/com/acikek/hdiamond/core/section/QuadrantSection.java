package com.acikek.hdiamond.core.section;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public interface QuadrantSection<E extends Enum<E>> extends DiamondSection<E> {

    EnumChatFormatting getTypeColor();

    private IChatComponent getTypeName() {
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
        return switch (getValue().ordinal()) {
            case 0, 1 -> EnumChatFormatting.GREEN;
            case 2, 3 -> EnumChatFormatting.YELLOW;
            case 4 -> EnumChatFormatting.RED;
            default -> EnumChatFormatting.WHITE;
        };
    }

    /*static <E extends Enum<E>> E fromJson(JsonElement element, Class<E> clazz) {
        JsonPrimitive primitive = element != null
                ? element.getAsJsonPrimitive()
                : null;
        if (primitive == null || primitive.isNumber()) {
            var list = EnumUtils.getEnumList(clazz);
            int index = primitive != null
                    ? primitive.getAsInt()
                    : 0;
            if (index < 0 || index >= list.size()) {
                throw new JsonParseException("index is out of range (0-" + (list.size() - 1) + ")");
            }
            return list.get(index);
        }
        if (primitive.isString()) {
            String str = primitive.getAsString();
            var result = EnumUtils.getEnumIgnoreCase(clazz, str);
            if (result == null) {
                throw new JsonParseException("unrecognized quadrant id '" + str + "'");
            }
            return result;
        }
        throw new JsonParseException("hazard quadrant must be a string or its number ordinal");
    }*/
}
