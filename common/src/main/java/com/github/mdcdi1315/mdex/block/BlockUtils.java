package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Runtime.CompilerServices.Extension;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.DisallowNull;

import com.github.mdcdi1315.mdex.util.BlockNotFoundException;
import com.github.mdcdi1315.mdex.util.BlockPropertyNotFoundException;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import java.util.function.ToIntFunction;

/**
 * Block utilities class. <br />
 * This class was created for abstracting how individual features of the mod should interact with the world blocks. <br />
 * This is intended to be a stable API, because Minecraft implementation details do continually change, thus this makes it easy to port to newer versions.
 */
@Extension
@SuppressWarnings("unused")
public final class BlockUtils
{
    private BlockUtils() {}

    /*
      TODO: Maybe implement in the future a seed generator for block positions.
      I have created one: (long)(((X << 16) & 0x0f0ff0ffL) ^ (0x000f0fffL * Y) ^ (((Z >> 16) * Z) & 0x3f0f00f5L))
     */

    /**
     * Gets a value whether this block instance represents an air block.
     * @param b The block to test.
     * @return A value whether the block passed is, in fact, air. <br /> Will additionally return {@code false} if {@code b} is {@code null}.
     */
    @Extension
    public static boolean IsAirBlock(Block b) {
        return b instanceof AirBlock;
    }

    /**
     * Gets a value whether this block instance represents a solid block.
     * @param b The block to test for solidness.
     * @return A value whether the block passed is, in fact, solid. <br /> Will additionally return {@code false} if {@code b} is {@code null}.
     */
    @Extension
    public static boolean IsSolidBlock(Block b) {
        if (b == null) { return false; }
        return !(b instanceof AirBlock);
    }

    @Extension
    public static boolean ReferentIsSolidBlock(BlockState bs)
    {
        if (bs == null) { return false; }
        return !bs.isAir();
    }

    /**
     * Gets a value whether the block that the passed {@link BlockState} represents is an air block, modded or not.
     * @param bs The {@link BlockState} instance to test.
     * @return A value whether the block state passed is air or not.
     */
    @Extension
    public static boolean ReferentIsAirBlock(BlockState bs)
    {
        if (bs == null) { return false; }
        return ReferentIsAirBlockUnsafe(bs);
    }

    /**
     * Unsafe variant of {@link #ReferentIsAirBlock(BlockState)} method.
     * @param bs The block state to test whether it is an air block.
     * @return A value whether the block state passed is air or not.
     */
    @Extension
    public static boolean ReferentIsAirBlockUnsafe(@DisallowNull BlockState bs) {
        return bs.isAir();
    }

    /**
     * Gets a value whether the block at the specified position is solid and the block above one is air block. <br />
     * Used by some features that want to enforce that the feature should be placed at a surface.
     * @param level The read-only level to read block positions from.
     * @param position The position where you want to test. Specifying null passes the first block in the first chunk (that is, 0 , 0 , 0).
     * @return The test result.
     */
    public static boolean BlockIsSolidAndAboveIsAir(BlockGetter level , BlockPos position)
        throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(level , "level");
        if (position == null) {
            position = new BlockPos(0 , 0 , 0);
        }
        return !level.getBlockState(position).isAir() && level.getBlockState(position.above()).isAir();
    }

    /**
     * Gets a value whether the block at the specified position is solid and the block above one is air block. <br />
     * Can be used for features that want to enforce that the feature should be placed at a ceiling or roof. <br />
     * This is the exact opposite condition of {@link BlockUtils#BlockIsSolidAndAboveIsAir(BlockGetter, BlockPos)}, which does check first for a solid block and then for an air block.
     * @param level The read-only level to read block positions from.
     * @param position The position where you want to test. Specifying null passes the first block in the first chunk (that is, 0 , 0 , 0).
     * @return The test result.
     */
    public static boolean BlockIsAirAndAboveSolid(BlockGetter level , BlockPos position)
    {
        ArgumentNullException.ThrowIfNull(level , "level");
        if (position == null) {
            position = new BlockPos(0 , 0 , 0);
        }
        return level.getBlockState(position).isAir() && !level.getBlockState(position.above()).isAir();
    }

    /**
     * Gets a value whether the specified position does not have an assigned fluid.
     * @param level The read-only level to read the block position from.
     * @param position The position where you want to test. Specifying null passes the first block in the first chunk (that is, 0 , 0 , 0).
     * @return The test result.
     */
    public static boolean IsEmptyFluid(BlockGetter level , BlockPos position)
    {
        ArgumentNullException.ThrowIfNull(level , "level");
        if (position == null) {
            position = new BlockPos(0 , 0 , 0);
        }
        return level.getFluidState(position).is(Fluids.EMPTY);
    }

    /**
     * Gets a value whether the specified position does have a fluid assigned.
     * @param level The read-only level to read the block position from.
     * @param position The position where you want to test. Specifying null passes the first block in the first chunk (that is, 0 , 0 , 0).
     * @return The test result.
     */
    public static boolean HasAnyFluid(BlockGetter level , BlockPos position)
    {
        ArgumentNullException.ThrowIfNull(level , "level");
        if (position == null) {
            position = new BlockPos(0 , 0 , 0);
        }
        return !level.getFluidState(position).is(Fluids.EMPTY);
    }

    /**
     * Gets a value whether the specified position does have a fluid assigned, and if yes, it schedules a tick to happen for that fluid.
     * @param level The level accessor to read the block position from, and to schedule the tick if required.
     * @param position The position where you want to test. Specifying null passes the first block in the first chunk (that is, 0 , 0 , 0).
     * @return The test result.
     */
    public static boolean IsHasAnyFluidScheduleTick(LevelAccessor level , BlockPos position)
    {
        ArgumentNullException.ThrowIfNull(level , "level");
        if (position == null) {
            position = new BlockPos(0 , 0 , 0);
        }
        var fs = level.getFluidState(position);
        if (!fs.is(Fluids.EMPTY)) {
            level.scheduleTick(position, fs.getType(), 0);
            return true;
        }
        return false;
    }

    /**
     * Assigns a loot table to a randomizable container block entity at the specified position. <br />
     * This operation requires the block at <code>pos</code> to be a {@link RandomizableContainerBlockEntity} object or a derivant class.
     * @param level The read-only level to read the container block entity from.
     * @param random The random source to use when the loot table will be assigned. Required for setting the seed to the loot table.
     * @param pos The absolute position of the randomizable container.
     * @param lootTable The loot table to assign to the container at <code>pos</code>.
     * @return A value whether the loot table was appended to the container or not. Use this value only when you want to be ensured about that the outcome is good.
     */
    public static boolean SetRandomizableContainerLootTable(BlockGetter level, RandomSource random, BlockPos pos, ResourceLocation lootTable)
    {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof RandomizableContainerBlockEntity e) {
            e.setLootTable(lootTable, random.nextLong());
            return true;
        }
        return false;
    }

    /**
     * Assigns a loot table to a randomizable container block entity at the specified position. <br />
     * This operation requires the block at <code>pos</code> to be a {@link RandomizableContainerBlockEntity} object or a derivant class.
     * @param level The read-only level to read the container block entity from.
     * @param random The random source to use when the loot table will be assigned. Required for setting the seed to the loot table.
     * @param pos The absolute position of the randomizable container.
     * @param lootTable The loot table to assign to the container at <code>pos</code>.
     * @return A value whether the loot table was appended to the container or not. Use this value only when you want to be ensured about that the outcome is good.
     */
    public static boolean SetRandomizableContainerLootTable(BlockGetter level, RandomSource random, BlockPos pos, ResourceKey<LootTable> lootTable)
    {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof RandomizableContainerBlockEntity e) {
            e.setLootTable(lootTable.location(), random.nextLong());
            return true;
        }
        return false;
    }

    public static String ConstructExactDescriptionID(String namespace, String path)
    {
        ArgumentNullException.ThrowIfNull(namespace , "namespace");
        ArgumentNullException.ThrowIfNull(path, "path");
        return Util.makeDescriptionId("block" , ResourceLocation.tryBuild(namespace , path));
    }

    public static String ConstructExactDescriptionID(String pathonly)
    {
        ArgumentNullException.ThrowIfNull(pathonly , "pathonly");
        return Util.makeDescriptionId("block" , ResourceLocation.tryBuild("" , pathonly));
    }

    // Generic declaration for returning the property comparison value from compareTo contexts
    private static <T extends Comparable<T>> int CompareProperties(Property<T> p , BlockState s1 , BlockState s2)
    {
        return s1.getValue(p).compareTo(s2.getValue(p));
    }

    private static boolean PropertyDoesNotMatch(BlockState s1 , BlockState s2 , Property<?> prop)
    {
        // Unknown (wildcard) declaration to generic translation.
        return CompareProperties(prop , s1 , s2) != 0;
    }

    public static boolean BlockStatesMatch(BlockState s1 , BlockState s2)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(s1);
        ArgumentNullException.ThrowIfNull(s2);
        // TODO: See whether the s1.equals(s2) check would work
        if (s1.getBlock() == s2.getBlock())
        {
            for (var p1 : s1.getProperties())
            {
                if (PropertyDoesNotMatch(s1 , s2 , p1))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static ToIntFunction<BlockState> GetBlockLightEmissionWhenLit(int lightValue) {
        return (p_50763_) -> (Boolean)p_50763_.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    /**
     * Gets the {@link Block} corresponding to the specified ID, or fails with {@link BlockNotFoundException}.
     */
    public static @NotNull Block GetBlockFromID(ResourceLocation location)
        throws BlockNotFoundException , ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(location , "location");
        return BuiltInRegistries.BLOCK.getOptional(location).orElseThrow(() -> new BlockNotFoundException(location));
    }

    /**
     * Finds the block property of block , or fails with exception.
     * @param bl The block whose property to find
     * @param prop The property that needs to be found on the block
     */
    public static void RequireBlockPropertyOrFail(@NotNull Block bl , String prop)
        throws BlockPropertyNotFoundException , ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(bl , "bl");
        if (bl.getStateDefinition().getProperty(prop) == null)
        {
            throw new BlockPropertyNotFoundException(prop);
        }
    }

    @Extension
    public static BlockPos AbsoluteXZ(BlockPos input)
    {
        return new BlockPos(Math.abs(input.getX()) , input.getY() , Math.abs(input.getZ()));
    }

    @Extension
    public static BlockPos AbsoluteAll(BlockPos input)
    {
        return new BlockPos(Math.abs(input.getX()) , Math.abs(input.getY()) , Math.abs(input.getZ()));
    }

    @Extension
    public static BlockPos RelativeUnmodifiedY(BlockPos pos, Direction direction , int amount)
    {
        return new BlockPos(pos.getX() + direction.getStepX() * amount , pos.getY() , pos.getZ() + direction.getStepZ() * amount);
    }
}
