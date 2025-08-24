package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.MDEXModConfig;
import com.github.mdcdi1315.mdex.api.MDEXModAPI;
import com.github.mdcdi1315.mdex.api.teleporter.MDEXTeleporterImplementation;
import com.github.mdcdi1315.mdex.block.entity.TeleporterTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Explosion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
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
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if (hand == InteractionHand.MAIN_HAND && player instanceof ServerPlayer sp)
        {
            if (TransferPlayer(sp, pos)) {
                MDEXBalmLayer.LOGGER.info("MDEXTELEPORTER_EVENTS: Player with UUID '{}' was successfully teleported to dimension with ID '{}' through Mining Dimension Teleporter mechanism." , sp.getUUID() , sp.level().dimensionTypeId().location());
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.SUCCESS;
    }

    public boolean TransferPlayer(ServerPlayer sp , BlockPos bps)
    {
        if (sp.getVehicle() != null || sp.isVehicle()) {
            return false;
        }

        Level sl = sp.level();
        ServerLevel destination;
        ResourceLocation dimid = sl.dimensionTypeId().location();
        if (dimid.equals(MDEXBalmLayer.MINING_DIM_IDENTIFIER)) {
            // Player must return to it's home
            destination = sp.server.getLevel(ResourceKey.create(Registries.DIMENSION, MDEXModConfig.getActive().HomeDimension));
        } else {
            // Player now goes to the Mining Dimension
            destination = sp.server.getLevel(ResourceKey.create(Registries.DIMENSION, MDEXBalmLayer.MINING_DIM_IDENTIFIER));
        }
        if (destination == null) {
            sp.displayClientMessage(Component.translatable("errormessage.wrong_travelling_dimension"), true);
            return false;
        }
        return MDEXModAPI.getMethodImplementation().ChangeDimension(sp , destination , new MDEXTeleporterImplementation(bps)) != null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TeleporterTileEntity(blockPos , blockState);
    }
}
