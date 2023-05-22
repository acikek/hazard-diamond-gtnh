package com.acikek.hdiamond.network;

import com.acikek.hdiamond.HDiamond;
import com.acikek.hdiamond.api.HazardDiamondAPI;
import com.acikek.hdiamond.api.util.HazardDataHolder;
import com.acikek.hdiamond.core.HazardData;
import com.acikek.hdiamond.entity.PanelEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

public class HDNetworking {

    public static final ResourceLocation UPDATE_PANEL = HDiamond.id("update_panel");
    public static final ResourceLocation OPEN_SCREEN = HDiamond.id("open_screen");

    @SideOnly(Side.CLIENT)
    public static void c2sUpdatePanelData(PanelEntity entity, HazardData data) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(entity.getEntityId());
        data.write(buf);
        ClientPlayNetworking.send(UPDATE_PANEL, buf);
    }

    public static void s2cOpenScreen(Collection<EntityPlayerMP> players, HazardDataHolder holder) {
        ByteBuf buf = Unpooled.buffer();
        holder.getHazardData().write(buf);
        for (var player : players) {
            ServerPlayNetworking.send(player, OPEN_SCREEN, buf);
        }
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_PANEL, (server, player, handler, buf, responseSender) -> {
            int id = buf.readInt();
            HazardData data = HazardData.read(buf);
            server.execute(() -> {
                Entity entity = player.getWorld().getEntityById(id);
                if (entity instanceof PanelEntity panelEntity) {
                    panelEntity.updateHazardData(data);
                }
            });
        });
    }

    @SideOnly(Side.CLIENT)
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(OPEN_SCREEN, (client, handler, buf, responseSender) -> {
            HazardData data = HazardData.read(buf);
            client.execute(() -> HazardDiamondAPI.open(data));
        });
    }
}
