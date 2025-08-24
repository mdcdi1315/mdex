package com.github.mdcdi1315.mdex.api.teleporter;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.MDEXModConfig;
import com.github.mdcdi1315.mdex.api.ITeleporter;
import com.github.mdcdi1315.mdex.block.ModBlocks;
import com.github.mdcdi1315.mdex.block.entity.TeleporterTileEntity;
import com.github.mdcdi1315.mdex.util.TwoIntegerVector;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.vehicle.DismountHelper;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


public class MDEXTeleporterImplementation
        implements ITeleporter
{

    private BlockPos pos;

    public MDEXTeleporterImplementation(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity e = repositionEntity.apply(false);
        if (!(e instanceof ServerPlayer player)) {
            return e;
        }
        LevelChunk chunk = (LevelChunk) destWorld.getChunk(pos);
        Vec3 spawnPos = findPortalInChunk(chunk);

        if (spawnPos == null) {
            if (destWorld.dimension().location().equals(MDEXBalmLayer.MINING_DIM_IDENTIFIER)) {
                spawnPos = placeTeleporterMining(destWorld, chunk);
            } else {
                spawnPos = placeTeleporterHome(destWorld, chunk);
            }
        }
        if (spawnPos == null) {
            return e;
        }

        player.giveExperienceLevels(0);
        player.teleportTo(spawnPos.x(), spawnPos.y(), spawnPos.z());
        return e;
    }

    @MaybeNull
    private Vec3 findPortalInChunk(LevelChunk chunk) {
        return chunk.getBlockEntities()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof TeleporterTileEntity)
                .sorted((o1, o2) -> {
                    if (MDEXModConfig.getActive().ShouldSpawnPortalInDeep) {
                        return Integer.compare(o1.getKey().getY(), o2.getKey().getY());
                    } else {
                        return o2.getKey().getY() - o1.getKey().getY();
                    }
                })
                .map(Map.Entry::getKey)
                .map(pos -> getTeleporterSpawnPos(chunk.getLevel(), pos))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private static Vec3 getTeleporterSpawnPos(Level level, BlockPos blockPos) {
        return DismountHelper.findSafeDismountLocation(EntityType.PLAYER, level, blockPos.above(), false);
    }

    private boolean isAirOrStone(LevelChunk chunk, BlockPos pos) {
        BlockState state = chunk.getBlockState(pos);
        return state.is(Blocks.STONE) || state.isAir();
    }

    private boolean isReplaceable(Level world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        return state.is(BlockTags.REPLACEABLE) ||
                state.getBlock().equals(Blocks.STONE) ||
                state.getBlock().equals(Blocks.GRANITE) ||
                state.getBlock().equals(Blocks.ANDESITE) ||
                state.getBlock().equals(Blocks.DIORITE) ||
                state.getBlock().equals(Blocks.DIRT) ||
                state.getBlock().equals(Blocks.GRAVEL) ||
                state.getBlock().equals(Blocks.LAVA) ||
                state.isAir();
    }

    private boolean SpaceIsEmpty(ServerLevel world , BlockPos absolutePos)
    {
        if (isReplaceable(world , absolutePos.above(3)))
        {
            for (Direction d : new Direction[] { Direction.NORTH , Direction.SOUTH , Direction.EAST , Direction.WEST })
            {
                for (int I = 1; I < 3; I++)
                {
                    if (isReplaceable(world , absolutePos.above(I).relative(d)) == false) { return false; }
                }
            }
            return true;
        }
        return false;
    }

    private void createTeleporterStartAreaMining(ServerLevel world , BlockPos absolutePos)
    {
        world.setBlockAndUpdate(absolutePos, ModBlocks.TELEPORTER.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(1), Blocks.AIR.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(2), Blocks.AIR.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(3), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(1).relative(Direction.NORTH), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(1).relative(Direction.SOUTH), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(1).relative(Direction.EAST), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(1).relative(Direction.WEST), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(2).relative(Direction.NORTH), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(2).relative(Direction.SOUTH), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(2).relative(Direction.EAST), Blocks.STONE.defaultBlockState());
        world.setBlockAndUpdate(absolutePos.above(2).relative(Direction.WEST), Blocks.STONE.defaultBlockState());
    }

    private Vec3 placeTeleporterMiningDeepMode(ServerLevel world , LevelChunk chunk , TwoIntegerVector minmax)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = minmax.getX(); y < minmax.getY() - 1; y++)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).isAir() && chunk.getBlockState(pos.above(1)).isAir() && chunk.getBlockState(pos.above(2)).isAir())
                    {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY(), pos.getZ());
                        world.setBlockAndUpdate(absolutePos, ModBlocks.TELEPORTER.defaultBlockState());
                        return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                    }
                }
            }
        }
        return null;
    }

    private Vec3 placeTeleporterMiningNormalMode(ServerLevel world , LevelChunk chunk , TwoIntegerVector minmax)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = minmax.getY() - 1; y >= minmax.getX(); y--)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).isAir() && chunk.getBlockState(pos.above(1)).isAir() && chunk.getBlockState(pos.above(2)).isAir())
                    {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY(), pos.getZ());
                        world.setBlockAndUpdate(absolutePos, ModBlocks.TELEPORTER.defaultBlockState());
                        return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                    }
                }
            }
        }
        return null;
    }

    private Vec3 placeTeleporterMiningDeepMode2(ServerLevel world , LevelChunk chunk , TwoIntegerVector minmax)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = minmax.getX(); y < minmax.getY() - 1; y++)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    pos.set(x, y, z);
                    if (isAirOrStone(chunk, pos) && isAirOrStone(chunk, pos.above(1)) && isAirOrStone(chunk, pos.above(2)))
                    {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos);
                        if (SpaceIsEmpty(world , absolutePos)) {
                            createTeleporterStartAreaMining(world , absolutePos);
                            return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Vec3 placeTeleporterMiningNormalMode2(ServerLevel world , LevelChunk chunk , TwoIntegerVector minmax)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = minmax.getY() - 1; y >= minmax.getX(); y--)
        {
            for (int x = 0; x < 16; x++)
            {
                for (int z = 0; z < 16; z++)
                {
                    pos.set(x, y, z);
                    if (isAirOrStone(chunk, pos) && isAirOrStone(chunk, pos.above(1)) && isAirOrStone(chunk, pos.above(2)))
                    {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos);
                        if (SpaceIsEmpty(world , absolutePos))
                        {
                            createTeleporterStartAreaMining(world , absolutePos);
                            return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Vec3 placeTeleporterMining(ServerLevel world, LevelChunk chunk)
    {
        boolean deep = MDEXModConfig.getActive().ShouldSpawnPortalInDeep;
        TwoIntegerVector minmax = new TwoIntegerVector(world.getMinBuildHeight() , world.getMaxBuildHeight() - 10);
        Vec3 result = deep ?
                placeTeleporterMiningDeepMode(world , chunk , minmax) :
                placeTeleporterMiningNormalMode(world , chunk , minmax);

        if (result == null)
        {
            result = deep ?
                    placeTeleporterMiningDeepMode2(world , chunk , minmax) :
                    placeTeleporterMiningNormalMode2(world , chunk , minmax);
        }

        return result;
    }

    private Vec3 placeTeleporterHome(ServerLevel world, LevelChunk chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = world.getMaxBuildHeight(); y >= world.getMinBuildHeight(); y--) {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).isAir() &&
                            chunk.getBlockState(pos.above(1)).isAir() &&
                            chunk.getBlockState(pos.above(2)).isAir() &&
                            !chunk.getBlockState(pos.below()).isAir()) {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY(), pos.getZ());
                        world.setBlockAndUpdate(absolutePos, ModBlocks.TELEPORTER.defaultBlockState());
                        return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                    }
                }
            }
        }

        return null;
    }

}