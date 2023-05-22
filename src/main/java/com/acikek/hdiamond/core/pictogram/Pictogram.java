package com.acikek.hdiamond.core.pictogram;

import com.acikek.hdiamond.core.section.DiamondSection;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.DataWatcher;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Pictogram implements DiamondSection<Pictogram> {

    EXPLOSIVE,
    FLAMMABLE,
    OXIDIZING,
    COMPRESSED_GAS,
    CORROSIVE,
    TOXIC,
    HARMFUL,
    HEALTH_HAZARD,
    ENVIRONMENTAL_HAZARD;

    private final Texture texture = new Texture((ordinal() * 32) % 160, 64 + (ordinal() >= 5 ? 32 : 0), 32, 32);

    @Override
    public Pictogram getValue() {
        return this;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getType() {
        return "ghs.hdiamond";
    }

    public static void writeNbt(NBTTagCompound nbt, Set<Pictogram> pictograms) {
        int[] ints = pictograms.stream()
                .map(Enum::ordinal)
                .sorted()
                .mapToInt(i -> i)
                .toArray();
        nbt.setIntArray("Pictograms", ints);
    }

    public static Set<Pictogram> readNbt(NBTTagCompound nbt) {
        return Arrays.stream(nbt.getIntArray("Pictograms"))
                .mapToObj(index -> Pictogram.values()[index])
                .collect(Collectors.toSet());
    }

    public static void write(ByteBuf buf, Set<Pictogram> pictograms) {
        buf.writeInt(pictograms.size());
        for (Pictogram pictogram : pictograms) {
            buf.writeInt(pictogram.ordinal());
        }
    }

    public static Set<Pictogram> read(ByteBuf buf) {
        Set<Pictogram> result = new HashSet<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            result.add(Pictogram.values()[buf.readInt()]);
        }
        return result;
    }

    public static void initDataWatcher(DataWatcher watcher) {
        watcher.addObject(6, 0b0);
    }

    public static void writeDataWatcher(DataWatcher watcher, Set<Pictogram> pictograms) {
        int result = 0b0;
        for (Pictogram pictogram : pictograms) {
            result += 0b1 << pictogram.ordinal();
        }
        watcher.updateObject(6, result);
    }

    public static Set<Pictogram> readDataWatcher(DataWatcher watcher) {
        Set<Pictogram> result = new HashSet<>();
        int data = watcher.getWatchableObjectInt(6);
        for (int i = 0; i < Pictogram.values().length; i++) {
            int value = data & (0b1 << i);
            if (value == 1) {
                result.add(Pictogram.values()[i]);
            }
        }
        return result;
    }
}
