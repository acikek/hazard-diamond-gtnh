package com.acikek.hdiamond.core;

import com.acikek.hdiamond.api.util.HazardDataHolder;
import com.acikek.hdiamond.core.pictogram.Pictogram;
import com.acikek.hdiamond.core.quadrant.SpecificHazard;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.DataWatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;

import java.util.*;

public class HazardData implements HazardDataHolder {

    private final HazardDiamond diamond;
    private final Set<Pictogram> pictograms;

    public HazardData(HazardDiamond diamond, Set<Pictogram> pictograms) {
        this.diamond = diamond;
        this.pictograms = pictograms;
    }

    @Override
    public HazardData getHazardData() {
        return this;
    }

    public static HazardData empty() {
        return new HazardData(HazardDiamond.empty(), new HashSet<>());
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Diamond", diamond.toNbt());
        Pictogram.writeNbt(nbt, pictograms);
        return nbt;
    }

    public static HazardData fromNbt(NBTTagCompound nbt) {
        HazardDiamond diamond = HazardDiamond.fromNbt(nbt.getCompoundTag("Diamond"));
        Set<Pictogram> pictograms = Pictogram.readNbt(nbt);
        return new HazardData(diamond, pictograms);
    }

    public void write(ByteBuf buf) {
        diamond.write(buf);
        Pictogram.write(buf, pictograms);
    }

    public static HazardData read(PacketBuffer buf) {
        HazardDiamond diamond = HazardDiamond.read(buf);
        Set<Pictogram> pictograms = Pictogram.read(buf);
        return new HazardData(diamond, pictograms);
    }

    public static void initDataWatcher(DataWatcher watcher) {
        HazardDiamond.initDataWatcher(watcher);
        Pictogram.initDataWatcher(watcher);
    }

    public void writeDataWatcher(DataWatcher watcher) {
        diamond.writeDataWatcher(watcher);
        Pictogram.writeDataWatcher(watcher, pictograms);
    }

    public static HazardData readDataWatcher(DataWatcher watcher) {
        HazardDiamond diamond = HazardDiamond.readDataWatcher(watcher);
        Set<Pictogram> pictograms = Pictogram.readDataWatcher(watcher);
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
        IChatComponent sep = new ChatComponentText(EnumChatFormatting.GRAY + "-");
        IChatComponent numerals = diamond().fire().get().getSymbol()
                .appendSibling(sep).appendSibling(diamond().health().get().getSymbol())
                .appendSibling(sep).appendSibling(diamond().reactivity().get().getSymbol());
        if (diamond().specific().get() != SpecificHazard.NONE) {
            numerals.appendSibling(sep).appendSibling(diamond().specific().get().getSymbol());
        }
        result.add(numerals);
        IChatComponent pictograms = new ChatComponentTranslation("tooltip.hdiamond.panel_item.pictograms", pictograms().size())
                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY));
        result.add(pictograms);
        return result;
    }

    public HazardDiamond diamond() {
        return diamond;
    }

    public Set<Pictogram> pictograms() {
        return pictograms;
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
}
