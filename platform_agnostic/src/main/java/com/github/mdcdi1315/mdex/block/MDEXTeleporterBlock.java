package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.networking.MDEXNetworking;
import com.github.mdcdi1315.mdex.block.entity.TeleporterTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MDEXTeleporterBlock
        extends MDEXBaseBlock
        implements EntityBlock
{
    public MDEXTeleporterBlock(Properties properties, ResourceLocation location)
    {
        super(properties, location);
        this.registerDefaultState(this.stateDefinition.any());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion)
    {
        if (!explosion.getBlockInteraction().shouldAffectBlocklikeEntities()) {
            return false;
        }
        return explosion.getDirectSourceEntity() instanceof Player;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance)
    {
        if (level.dimension().location().equals(MDEXModInstance.MINING_DIM_IDENTIFIER)) {
            entity.causeFallDamage(fallDistance , 1.062f , entity.damageSources().fall());
        } else {
            entity.causeFallDamage(fallDistance , 1.00022f , entity.damageSources().fall());
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (player.getUsedItemHand() == InteractionHand.MAIN_HAND)
        {
            if (player.getVehicle() != null || player.isVehicle()) { return InteractionResult.FAIL; }
            MDEXNetworking.HandleTeleportRequest(player, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TeleporterTileEntity(blockPos , blockState);
    }
}