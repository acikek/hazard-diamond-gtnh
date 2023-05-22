package com.acikek.hdiamond.api.block;

import com.acikek.hdiamond.api.HazardDiamondAPI;
import com.acikek.hdiamond.api.util.HazardDataHolder;
import com.acikek.hdiamond.core.HazardData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * A block base that, when interacted with, displays immutable {@link HazardData}.<br>
 * This is not automatically included in
 */
public class BlockWithHazardData extends Block implements HazardDataHolder {

    private final HazardData data;

    protected BlockWithHazardData(Material materialIn, HazardData data) {
        super(materialIn);
        this.data = data;
    }

    @Override
    public HazardData getHazardData() {
        return data;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        if (worldIn.isRemote) {
            HazardDiamondAPI.open(this);
        }
        return worldIn.isRemote;
    }
}
