package com.acikek.hdiamond.client.screen;

import com.acikek.hdiamond.client.HDiamondClient;
import com.acikek.hdiamond.core.HazardData;
import com.acikek.hdiamond.core.pictogram.Pictogram;
import com.acikek.hdiamond.core.quadrant.QuadrantValue;
import com.acikek.hdiamond.core.quadrant.SpecificHazard;
import com.acikek.hdiamond.core.section.DiamondSection;
import com.acikek.hdiamond.entity.PanelEntity;
import com.acikek.hdiamond.network.HDNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class HazardScreen extends GuiScreen {

    public int x;
    public int y;
    public boolean movedCursor = false;

    public PanelEntity entity;
    public boolean isEditable;
    public HazardData originalData;
    public ResourceLocation id;
    public HazardData data;

    HazardScreen(PanelEntity entity, boolean isEditable, HazardData originalData, ResourceLocation id, HazardData data) {
        this.entity = entity;
        this.isEditable = isEditable;
        this.originalData = originalData;
        this.id = id;
        this.data = data;
    }

    public HazardScreen(PanelEntity entity) {
        this(entity, !entity.isWaxed(), null, null, entity.getHazardData().copy());
    }

    public HazardScreen(HazardData data) {
        this(null, false, null, null, data);
    }

    public HazardScreen(HazardData data, ResourceLocation id) {
        this(null, true, data.copy(), id, data);
    }

    public void addQuadrant(QuadrantValue<?> quadrant, int id, int halfX, int halfY) {
        buttonList.add(new DiamondWidget.Quadrant(this, quadrant, id, halfX, halfY, 62));
    }

    public void addPictogram(Pictogram pictogram, int id, int halfX, int halfY) {
        buttonList.add(new DiamondWidget.PictogramLabel(this, pictogram, id, halfX, halfY, 66));
    }

    @Override
    public void initGui() {
        this.x = (this.width - 128) / 4;
        this.y = (this.height) / 4 - 2;
        addQuadrant(data.diamond().fire(), 0, x + 16, y - 50);
        addQuadrant(data.diamond().health(), 1, x, y - 34);
        addQuadrant(data.diamond().reactivity(), 2, x + 32, y - 34);
        addQuadrant(data.diamond().specific(), 3, x + 16, y - 18);
        for (int i = 0; i < Pictogram.values().length; i++) {
            Pictogram pictogram = Pictogram.values()[i];
            addPictogram(pictogram, 4 + i, x - 57 + i * 18, y + 3 + (i % 2 == 0 ? 18 : 0));
        }
    }

    public void renderTooltip(List<IChatComponent> text, int x, int y) {
        drawHoveringText(text, x, y, fontRendererObj);
    }

    public static void setTexture() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(HDiamondClient.WIDGETS);
    }

    public void renderPanel() {
        drawTexturedModalRect(x, y - 50, 0, 0, 64, 64);
    }

    public void renderElement(DiamondSection<?> element, int x, int y) {
        DiamondSection.Texture texture = element.getTexture();
        drawTexturedModalRect(x, y, texture.u(), texture.v(), texture.width(), texture.height());
    }

    public void renderQuadrants() {
        renderElement(data.diamond().fire().get(), x + 26, y - 41);
        renderElement(data.diamond().health().get(), x + 10, y - 25);
        renderElement(data.diamond().reactivity().get(), x + 42, y - 25);
        SpecificHazard specific = data.diamond().specific().get();
        if (specific != SpecificHazard.NONE) {
            boolean rad = specific == SpecificHazard.RADIOACTIVE;
            renderElement(specific, x + 23 - (rad ? 1 : 0), y - 9 - (rad ? 2 : 0));
        }
    }

    public void renderPictogram(Pictogram pictogram, int x, int y) {
        float color = data.pictograms().contains(pictogram) ? 1.0f : 0.5f;
        GL11.glColor4f(color, color, color, 1.0f);
        renderElement(pictogram, x, y);
    }

    public void renderPictograms() {
        for (int i = 0; i < Pictogram.values().length; i++) {
            Pictogram pictogram = Pictogram.values()[i];
            renderPictogram(pictogram, x - 56 + i * 18, y + 4 + (i % 2 == 0 ? 18 : 0));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        setTexture();
        GL11.glPushMatrix();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        renderPanel();
        renderQuadrants();
        renderPictograms();
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        super.mouseMovedOrUp(mouseX, mouseY, state);
        if (!movedCursor) {
            movedCursor = true;
        }
    }

    @Override
    public void onGuiClosed() {
        if (entity != null) {
            HDNetworking.c2sUpdatePanelData(entity, data);
        }
        else if (originalData != null) {
            //HazardScreenEdited.EVENT.invoker().onEdit(Minecraft.getMinecraft().thePlayer, originalData, data, id);
        }
        super.onGuiClosed();
    }
}
