package com.acikek.hdiamond.core;

import com.acikek.hdiamond.api.util.HazardDataHolder;
import com.acikek.hdiamond.core.pictogram.Pictogram;
import com.acikek.hdiamond.core.quadrant.SpecificHazard;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record HazardData(HazardDiamond diamond, Set<Pictogram> pictograms) implements HazardDataHolder {

    /*public static final TrackedDataHandler<HazardData> DATA_TRACKER = TrackedDataHandler.of(
            (buf, data) -> data.write(buf),
            HazardData::read
    );*/

    @Override
    public HazardData getHazardData() {
        return this;
    }

    public static HazardData empty() {
        return new HazardData(HazardDiamond.empty(), new HashSet<>());
    }

    /*public static HazardData fromJson(JsonObject obj) {
        HazardDiamond diamond = obj.has("diamond")
                ? HazardDiamond.fromJson(Jso.getObject(obj, "diamond"))
                : HazardDiamond.empty();
        Set<Pictogram> pictograms = obj.has("pictograms")
                ? Pictogram.fromJson(obj)
                : Collections.emptySet();
        return new HazardData(diamond, pictograms);
    }*/

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Diamond", diamond.toNbt());
        Pictogram.writeNbt(nbt, pictograms);
        return nbt;
    }

    public static HazardData fromNbt(NBTTagCompound nbt) {
        var diamond = HazardDiamond.fromNbt(nbt.getCompoundTag("Diamond"));
        var pictograms = Pictogram.readNbt(nbt);
        return new HazardData(diamond, pictograms);
    }

    public void write(PacketBuffer buf) {
        diamond.write(buf);
        Pictogram.write(buf, pictograms);
    }

    public static HazardData read(PacketBuffer buf) {
        var diamond = HazardDiamond.read(buf);
        var pictograms = Pictogram.read(buf);
        return new HazardData(diamond, pictograms);
    }

    public HazardData copy() {
        return new HazardData(diamond.copy(), new HashSet<>(pictograms));
    }

    public boolean isEmpty() {
        return diamond().isEmpty() && pictograms().isEmpty();
    }

    public List<IChatComponent> getTooltip() {
        List<IChatComponent> result = new ArrayList<>();
        var sep = new ChatComponentText(EnumChatFormatting.GRAY + "-");
        var numerals = diamond().fire().get().getSymbol()
                .appendSibling(sep).appendSibling(diamond().health().get().getSymbol())
                .appendSibling(sep).appendSibling(diamond().reactivity().get().getSymbol());
        if (diamond().specific().get() != SpecificHazard.NONE) {
            numerals.appendSibling(sep).appendSibling(diamond().specific().get().getSymbol());
        }
        result.add(numerals);
        var pictograms = new ChatComponentTranslation("tooltip.hdiamond.panel_item.pictograms", pictograms().size())
                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY));
        result.add(pictograms);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HazardData data = (HazardData) o;
        if (!diamond.equals(data.diamond)) {
            return false;
        }
        return pictograms.equals(data.pictograms);
    }

    @Override
    public int hashCode() {
        int result = diamond.hashCode();
        result = 31 * result + pictograms.hashCode();
        return result;
    }

    /*public static void register() {
        TrackedDataHandlerRegistry.register(DATA_TRACKER);
    }*/
}
