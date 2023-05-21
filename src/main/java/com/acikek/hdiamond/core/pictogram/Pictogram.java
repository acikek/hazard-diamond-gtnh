package com.acikek.hdiamond.core.pictogram;

import com.acikek.hdiamond.core.section.DiamondSection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

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

    /*public static Set<Pictogram> fromJson(JsonObject obj) {
        Set<Pictogram> result = new HashSet<>();
        for (var pictogram : JsonHelper.getArray(obj, "pictograms")) {
            String str = pictogram.getAsString();
            Pictogram p = EnumUtils.getEnumIgnoreCase(Pictogram.class, str);
            if (p == null) {
                throw new JsonParseException("unrecognized pictogram '" + str + "'");
            }
            result.add(p);
        }
        return result;
    }*/

    public static void writeNbt(NBTTagCompound nbt, Set<Pictogram> pictograms) {
        var ints = pictograms.stream()
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

    public static void write(PacketBuffer buf, Set<Pictogram> pictograms) {
        buf.writeInt(pictograms.size());
        for (var pictogram : pictograms) {
            buf.writeInt(pictogram.ordinal());
        }
    }

    public static Set<Pictogram> read(PacketBuffer buf) {
        Set<Pictogram> result = new HashSet<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            result.add(Pictogram.values()[buf.readInt()]);
        }
        return result;
    }
}
