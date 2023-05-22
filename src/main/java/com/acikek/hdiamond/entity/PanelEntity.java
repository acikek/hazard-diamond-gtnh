package com.acikek.hdiamond.entity;

import com.acikek.hdiamond.api.HazardDiamondAPI;
import com.acikek.hdiamond.api.util.HazardDataHolder;
import com.acikek.hdiamond.core.HazardData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PanelEntity extends EntityHanging implements HazardDataHolder {

    public PanelEntity(World p_i1588_1_) {
        super(p_i1588_1_);
    }

    public PanelEntity(World p_i1589_1_, int p_i1589_2_, int p_i1589_3_, int p_i1589_4_, int p_i1589_5_) {
        super(p_i1589_1_, p_i1589_2_, p_i1589_3_, p_i1589_4_, p_i1589_5_);
        setDirection(p_i1589_5_);
    }

    @Override
    protected void entityInit() {
        HazardData.initDataWatcher(getDataWatcher());
        getDataWatcher().addObject(7, 0);
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        ItemStack stack = player.getHeldItem();
        if (player.isSneaking() && stack.getItem() == Items.slime_ball) {
            if (isWaxed()) {
                return false;
            }
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize -= 1;
            }
            playSound("mob.slime.small", 1.0f, 1.0f);
            getDataWatcher().updateObject(7, 1);
            getDataWatcher().setObjectWatched(7);
        }
        else if (player.worldObj.isRemote) {
            HazardDiamondAPI.open(this);
        }
        return player.worldObj.isRemote;
    }

    @Override
    public int getWidthPixels() {
        return 14;
    }

    @Override
    public int getHeightPixels() {
        return 14;
    }

    @Override
    public void onBroken(Entity p_110128_1_) {

    }

    @Override
    public HazardData getHazardData() {
        return HazardData.readDataWatcher(getDataWatcher());
    }

    public boolean isWaxed() {
        return getDataWatcher().getWatchableObjectInt(7) == 1;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        HazardData.fromNbt(nbt.getCompoundTag("HazardData")).writeDataWatcher(getDataWatcher());
        getDataWatcher().updateObject(7, nbt.getBoolean("Waxed") ? 1 : 0);
        getDataWatcher().setObjectWatched(7);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setTag("HazardData", getHazardData().toNbt());
        nbt.setBoolean("Waxed", isWaxed());
    }
}
