package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.TeleportingManager;
import com.github.mdcdi1315.mdex.api.TeleportRequestState;
import com.github.mdcdi1315.mdex.block.entity.TeleporterTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Explosion;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MDEXTeleporterBlock
        extends MDEXBaseBlock
        implements EntityBlock
{
    public MDEXTeleporterBlock(Properties properties , String descid)
    {
        super(properties , descid);
        this.registerDefaultState(this.stateDefinition.any());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion)
    {
        if (explosion.interactsWithBlocks() == false)
        {
            return false;
        }
        return explosion.getDirectSourceEntity() instanceof Player;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance)
    {
        if (level.dimension().location().equals(MDEXBalmLayer.MINING_DIM_IDENTIFIER)) {
            entity.causeFallDamage(fallDistance , 1.062f , entity.damageSources().fall());
        } else {
            entity.causeFallDamage(fallDistance , 1.00022f , entity.damageSources().fall());
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.MAIN_HAND)
        {
            if (TransferPlayer(player, pos)) {
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.SUCCESS;
    }

    public boolean TransferPlayer(Player sp , BlockPos bps)
    {
        if (sp.getVehicle() != null || sp.isVehicle()) {
            return false;
        }

        TeleportingManager manager = MDEXModAPI.getMethodImplementation().GetTeleportingManager();
        TeleportRequestState state = manager.Teleport(sp , bps);
        if (state == TeleportRequestState.SCHEDULED) {
            sp.displayClientMessage(Component.translatable("mdex.teleportmanager.msg.teleport_scheduled") , true);
        }
        return state != TeleportRequestState.FAILED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TeleporterTileEntity(blockPos , blockState);
    }
}