package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentNullException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.NotNull;
import com.github.mdcdi1315.DotNetLayer.System.Exception;
import com.github.mdcdi1315.DotNetLayer.System.Runtime.CompilerServices.Extension;
import com.github.mdcdi1315.mdex.util.BlockNotFoundException;
import com.github.mdcdi1315.mdex.util.BlockPropertyNotFoundException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.ToIntFunction;

@Extension
@SuppressWarnings("unused")
public final class BlockUtils
{
    private BlockUtils() {}

    @Extension
    public static boolean IsAirBlock(Block b)
    {
        return b instanceof AirBlock;
    }

    @Extension
    public static boolean IsSolidBlock(Block b) { return !(b instanceof AirBlock); }

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

    public static boolean ReferentIsSolidBlock(BlockState bs)
    {
        if (bs == null) { return false; }
        return !bs.isAir();
    }

    public static boolean ReferentIsAirBlock(BlockState bs)
    {
        if (bs == null) { return false; }
        return ReferentIsAirBlockUnsafe(bs);
    }

    public static boolean ReferentIsAirBlockUnsafe(BlockState bs)
    {
        return bs.isAir();
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

    private static boolean NotEqualToCompareToWithReflection(Comparable<?> v1 , Object v2)
    {
        try {
            return (Integer) v1.getClass().getMethod("compareTo").invoke(v1 , v2) != 0;
        } catch (java.lang.Exception e) {
            return true;
        }
    }

    public static boolean BlockStatesMatch(BlockState s1 , BlockState s2)
    {
        ArgumentNullException.ThrowIfNull(s1);
        ArgumentNullException.ThrowIfNull(s2);
        if (s1.getBlock() == s2.getBlock())
        {
            for (var p1 : s1.getProperties())
            {
                if (NotEqualToCompareToWithReflection(s1.getValue(p1) , s2.getValue(p1)))
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
    @SuppressWarnings("all")
    public static @NotNull Block GetBlockFromID(ResourceLocation location)
        throws BlockNotFoundException , ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(location , "location");
        Block blk = null;
        try {
            blk = BuiltInRegistries.BLOCK.get(location);
        } catch (Exception e) {
            throw new BlockNotFoundException(location);
        }
        if (blk == null) {
            throw new BlockNotFoundException(location);
        }
        return blk;
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
