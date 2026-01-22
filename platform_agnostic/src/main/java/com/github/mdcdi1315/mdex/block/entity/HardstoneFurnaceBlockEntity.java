package com.github.mdcdi1315.mdex.block.entity;

import com.github.mdcdi1315.mdex.block.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class HardstoneFurnaceBlockEntity
    extends AbstractFurnaceBlockEntity
{
    public HardstoneFurnaceBlockEntity(BlockPos pos, BlockState block_state) {
        super(ModBlocks.HARDSTONE_FURNACE_ENTITY, pos, block_state, RecipeType.SMELTING);
    }

    // The below constructor is provided for mods that want to provide their own furnace block!
    public HardstoneFurnaceBlockEntity(BlockEntityType<?> entity_type, BlockPos pos, BlockState block_state) {
        super(entity_type, pos, block_state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() { return Component.translatable("block.mdex.hardstone_furnace"); }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FurnaceMenu(id, inventory, this, dataAccess);
    }
}
