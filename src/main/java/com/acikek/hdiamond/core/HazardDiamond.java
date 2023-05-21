package com.acikek.hdiamond.core;

import com.acikek.hdiamond.core.quadrant.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public record HazardDiamond(
        QuadrantValue<FireHazard> fire,
        QuadrantValue<HealthHazard> health,
        QuadrantValue<Reactivity> reactivity,
        QuadrantValue<SpecificHazard> specific) {

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

    /*public static HazardDiamond fromJson(JsonObject obj) {
        var fire = QuadrantSection.fromJson(obj.get("fire"), FireHazard.class);
        var health = QuadrantSection.fromJson(obj.get("health"), HealthHazard.class);
        var reactivity = QuadrantSection.fromJson(obj.get("reactivity"), Reactivity.class);
        var specific = obj.has("specific")
                ? QuadrantSection.fromJson(obj.get("specific"), SpecificHazard.class)
                : SpecificHazard.NONE;
        return new HazardDiamond(fire, health, reactivity, specific);
    }*/

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Fire", fire.get().ordinal());
        nbt.setInteger("Health", health.get().ordinal());
        nbt.setInteger("Reactivity", reactivity.get().ordinal());
        nbt.setInteger("Specific", specific.get().ordinal());
        return nbt;
    }

    public static HazardDiamond fromNbt(NBTTagCompound nbt) {
        var fire = FireHazard.values()[nbt.getInteger("Fire")];
        var health = HealthHazard.values()[nbt.getInteger("Health")];
        var reactivity = Reactivity.values()[nbt.getInteger("Reactivity")];
        var specific = SpecificHazard.values()[nbt.getInteger("Specific")];
        return new HazardDiamond(fire, health, reactivity, specific);
    }

    public void write(PacketBuffer buf) {
        buf.writeInt(fire.get().ordinal());
        buf.writeInt(health.get().ordinal());
        buf.writeInt(reactivity.get().ordinal());
        buf.writeInt(specific.get().ordinal());
    }

    public static HazardDiamond read(PacketBuffer buf) {
        var fire = FireHazard.values()[buf.readInt()];
        var health = HealthHazard.values()[buf.readInt()];
        var reactivity = Reactivity.values()[buf.readInt()];
        var specific = SpecificHazard.values()[buf.readInt()];
        return new HazardDiamond(fire, health, reactivity, specific);
    }

    public HazardDiamond copy() {
        return new HazardDiamond(fire.copy(), health.copy(), reactivity.copy(), specific.copy());
    }

    public boolean isEmpty() {
        return fire.isEmpty() && health().isEmpty() && reactivity().isEmpty() && specific.isEmpty();
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
