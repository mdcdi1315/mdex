package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.block.entity.HardstoneFurnaceBlockEntity;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public class MDEXHardstoneFurnaceBlock
    extends AbstractFurnaceBlock
{
    public static final MapCodec<MDEXHardstoneFurnaceBlock> CODEC = simpleCodec(MDEXHardstoneFurnaceBlock::new);

    public MDEXHardstoneFurnaceBlock(Properties properties) {
        this(properties, MDEXModInstance.BlockID("hardstone_furnace"));
    }

    public MDEXHardstoneFurnaceBlock(Properties properties, ResourceLocation location)
    {
        super(
                properties
                    .overrideDescription(BlockUtils.ConstructExactDescriptionID(location))
                    .setId(ResourceKey.create(Registries.BLOCK , location))
        );
    }

    @Override
    protected MapCodec<MDEXHardstoneFurnaceBlock> codec() { return CODEC; }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player)
    {
        if (level.getBlockEntity(pos) instanceof HardstoneFurnaceBlockEntity p) {
            player.openMenu(p);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new HardstoneFurnaceBlockEntity(pos, state); }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, ModBlocks.HARDSTONE_FURNACE_ENTITY);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
    {
        if (state.getValue(LIT))
        {
            double x = (double)pos.getX() + 0.5D;
            double y = pos.getY();
            double z = (double)pos.getZ() + 0.5D;
            if (random.nextDouble() < 0.1D) {
                level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.getValue(FACING);
            Direction.Axis axis = direction.getAxis();
            double d4 = (random.nextDouble() * 0.6D) - 0.3D;
            double d5 = axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
            double d6 = random.nextDouble() * 6.0D / 16.0D;
            double d7 = axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
            level.addParticle(ParticleTypes.SMOKE, x + d5, y + d6, z + d7, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, x + d5, y + d6, z + d7, 0.0D, 0.0D, 0.0D);
        }
    }
}
