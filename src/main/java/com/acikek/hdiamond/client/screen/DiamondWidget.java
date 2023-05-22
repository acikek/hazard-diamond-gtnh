package com.acikek.hdiamond.client.screen;

import com.acikek.hdiamond.core.pictogram.Pictogram;
import com.acikek.hdiamond.core.quadrant.QuadrantValue;
import com.acikek.hdiamond.core.section.DiamondSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class DiamondWidget extends GuiButton {

    public HazardScreen screen;
    public Polygon diamond;

    public DiamondWidget(HazardScreen screen, int id, int x, int y, int width, int height, IChatComponent message) {
        super(id, x, y, width, height, message.getFormattedText());
        this.screen = screen;
        this.diamond = new Polygon(
                new int[] { x, x + width / 2, x + width, x + width / 2 },
                new int[] { y + height / 2, y + height, y + height / 2, y },
                4
        );
    }

    public DiamondWidget(HazardScreen screen, int id, int halfX, int halfY, int size, IChatComponent message) {
        this(screen, id, (halfX * 2) + 1, (halfY * 2) + 1, size, size, message);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.field_146123_n = diamond.contains(mouseX, mouseY);
        if (this.field_146123_n) {
            renderTooltip(mouseX, mouseY);
        }
    }

    public abstract void renderTooltip(int mouseX, int mouseY);

    public void renderDiamondTooltip(DiamondSection<?> section, int mouseX, int mouseY, boolean off) {
        if (!screen.movedCursor) {
            return;
        }
        List<IChatComponent> tooltip = new ArrayList<>();
        tooltip.add(section.getTitle().setChatStyle(section.getTitle().getChatStyle().setColor(off ? EnumChatFormatting.GRAY : EnumChatFormatting.WHITE)));
        tooltip.add(section.getDescription().setChatStyle(new ChatStyle().setColor(off ? EnumChatFormatting.DARK_GRAY : EnumChatFormatting.GRAY)));
        screen.renderTooltip(tooltip, mouseX, mouseY);
    }

    @Override
    public int getHoverState(boolean mouseOver) {
        if (!screen.isEditable) {
            return 0;
        }
        return mouseOver ? 1 : 2;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.field_146123_n) {
            super.mousePressed(mc, mouseX, mouseY);
        }
        return this.field_146123_n;
    }

    public static void playSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public static class Quadrant extends DiamondWidget {

        public QuadrantValue<?> quadrant;

        public Quadrant(HazardScreen screen, QuadrantValue<?> quadrant, int id, int halfX, int halfY, int size) {
            super(screen, id, halfX, halfY, size, quadrant.get().getTitle());
            this.quadrant = quadrant;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (!screen.isEditable) {
                return false;
            }
            quadrant.scroll();
            playSound();
            return super.mousePressed(mc, mouseX, mouseY);
        }

        @Override
        public void renderTooltip(int mouseX, int mouseY) {
            renderDiamondTooltip(quadrant.get(), mouseX, mouseY, false);
        }
    }

    public static class PictogramLabel extends DiamondWidget {

        public Pictogram pictogram;

        public PictogramLabel(HazardScreen screen, Pictogram pictogram, int id, int halfX, int halfY, int size) {
            super(screen, id, halfX, halfY, size, pictogram.getTitle());
            this.pictogram = pictogram;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (!screen.isEditable) {
                return false;
            }
            Set<Pictogram> pictograms = screen.data.pictograms();
            if (pictograms.contains(pictogram)) {
                pictograms.remove(pictogram);
            }
            else {
                pictograms.add(pictogram);
            }
            playSound();
            return super.mousePressed(mc, mouseX, mouseY);
        }

        @Override
        public void renderTooltip(int mouseX, int mouseY) {
            renderDiamondTooltip(pictogram, mouseX, mouseY, !screen.data.pictograms().contains(pictogram));
        }
    }
}
