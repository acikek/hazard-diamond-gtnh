package com.acikek.hdiamond.core;

import com.acikek.hdiamond.core.quadrant.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.DataWatcher;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HazardDiamond {

    private final QuadrantValue<FireHazard> fire;
    private final QuadrantValue<HealthHazard> health;
    private final QuadrantValue<Reactivity> reactivity;
    private final QuadrantValue<SpecificHazard> specific;

    public HazardDiamond(QuadrantValue<FireHazard> fire, QuadrantValue<HealthHazard> health, QuadrantValue<Reactivity> reactivity, QuadrantValue<SpecificHazard> specific) {
        this.fire = fire;
        this.health = health;
        this.reactivity = reactivity;
        this.specific = specific;
    }

    public static HazardDiamond empty() {
        return new HazardDiamond(FireHazard.NONFLAMMABLE, HealthHazard.NORMAL, Reactivity.STABLE, SpecificHazard.NONE);
    }

    public HazardDiamond(FireHazard fire, HealthHazard health, Reactivity reactivity, SpecificHazard specific) {
        this(
                new QuadrantValue<>(FireHazard.class, fire),
                new QuadrantValue<>(HealthHazard.class, health),
                new QuadrantValue<>(Reactivity.class, reactivity),
                new QuadrantValue<>(SpecificHazard.class, specific)
        );
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Fire", fire.get().ordinal());
        nbt.setInteger("Health", health.get().ordinal());
        nbt.setInteger("Reactivity", reactivity.get().ordinal());
        nbt.setInteger("Specific", specific.get().ordinal());
        return nbt;
    }

    public static HazardDiamond fromNbt(NBTTagCompound nbt) {
        FireHazard fire = FireHazard.values()[nbt.getInteger("Fire")];
        HealthHazard health = HealthHazard.values()[nbt.getInteger("Health")];
        Reactivity reactivity = Reactivity.values()[nbt.getInteger("Reactivity")];
        SpecificHazard specific = SpecificHazard.values()[nbt.getInteger("Specific")];
        return new HazardDiamond(fire, health, reactivity, specific);
    }

    public List<QuadrantValue<?>> getQuadrants() {
        return Arrays.asList(fire, health, reactivity, specific);
    }

    public void write(ByteBuf buf) {
        for (QuadrantValue<?> quadrant : getQuadrants()) {
            buf.writeInt(quadrant.get().ordinal());
        }
    }

    public static HazardDiamond fromIndices(int fire, int health, int reactivity, int specific) {
        FireHazard fireValue = FireHazard.values()[fire];
        HealthHazard healthValue = HealthHazard.values()[health];
        Reactivity reactivityValue = Reactivity.values()[reactivity];
        SpecificHazard specificValue = SpecificHazard.values()[specific];
        return new HazardDiamond(fireValue, healthValue, reactivityValue, specificValue);
    }

    public static HazardDiamond read(ByteBuf buf) {
        return fromIndices(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void initDataWatcher(DataWatcher watcher) {
        List<QuadrantValue<?>> quadrants = empty().getQuadrants();
        for (int i = 0; i < quadrants.size(); i++) {
            watcher.addObject(2 + i, quadrants.get(i).get().ordinal());
        }
    }

    public void writeDataWatcher(DataWatcher watcher) {
        List<QuadrantValue<?>> quadrants = getQuadrants();
        for (int i = 0; i < quadrants.size(); i++) {
            watcher.updateObject(2 + i, quadrants.get(i).get().ordinal());
            watcher.setObjectWatched(2 + i);
        }
    }

    public static HazardDiamond readDataWatcher(DataWatcher watcher) {
        return fromIndices(
            watcher.getWatchableObjectInt(2),
            watcher.getWatchableObjectInt(3),
            watcher.getWatchableObjectInt(4),
            watcher.getWatchableObjectInt(5)
        );
    }

    public HazardDiamond copy() {
        return new HazardDiamond(fire.copy(), health.copy(), reactivity.copy(), specific.copy());
    }

    public boolean isEmpty() {
        return fire.isEmpty() && health.isEmpty() && reactivity.isEmpty() && specific.isEmpty();
    }

    public QuadrantValue<FireHazard> fire() {
        return fire;
    }

    public QuadrantValue<HealthHazard> health() {
        return health;
    }

    public QuadrantValue<Reactivity> reactivity() {
        return reactivity;
    }

    public QuadrantValue<SpecificHazard> specific() {
        return specific;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HazardDiamond that = (HazardDiamond) o;
        if (!fire.equals(that.fire)) {
            return false;
        }
        if (!health.equals(that.health)) {
            return false;
        }
        if (!reactivity.equals(that.reactivity)) {
            return false;
        }
        return specific.equals(that.specific);
    }

    @Override
    public int hashCode() {
        int result = fire.hashCode();
        result = 31 * result + health.hashCode();
        result = 31 * result + reactivity.hashCode();
        result = 31 * result + specific.hashCode();
        return result;
    }
}
