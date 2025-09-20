package com.github.mdcdi1315.mdex.features;

import com.github.mdcdi1315.DotNetLayer.System.*;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.util.RectAreaIterable;
import com.github.mdcdi1315.mdex.features.config.ModdedFeatureConfiguration;

import net.minecraft.tags.TagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import com.github.mdcdi1315.mdex.util.WeightedEntityEntry;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;


import java.lang.Exception;
import java.util.List;

@SuppressWarnings("unused")
public final class FeaturePlacementUtils
{
    private FeaturePlacementUtils() {}


    // data parameter is a request dependent value, so it can be null if the caller does not require it.
    public static <TF extends PlacedFeature , TData> void ForEachInHolderSet(HolderSet<TF> hs , @MaybeNull TData data , Action2<TF , TData> action)
    {
        ArgumentNullException.ThrowIfNull(hs , "hs");
        ArgumentNullException.ThrowIfNull(action , "action");
        ForEachInHolderSetUnsafe(hs , data , action);
    }

    // data1 and data2 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2> void ForEachInHolderSet(HolderSet<TF> hs , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , Action3<TF , TData1 , TData2> action)
    {
        ArgumentNullException.ThrowIfNull(hs , "hs");
        ArgumentNullException.ThrowIfNull(action , "action");
        ForEachInHolderSetUnsafe(hs , data1 , data2 , action);
    }

    // data1, data2 and data3 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2 , TData3> void ForEachInHolderSet(HolderSet<TF> hs , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , @MaybeNull TData3 data3 , Action4<TF , TData1 , TData2 , TData3> action)
    {
        ArgumentNullException.ThrowIfNull(hs , "hs");
        ArgumentNullException.ThrowIfNull(action , "action");
        ForEachInHolderSetUnsafe(hs , data1 , data2 , data3 , action);
    }

    // data parameter is a request dependent value, so it can be null if the caller does not require it.
    public static <TF extends PlacedFeature , TData> void ForEachInHolderSetUnsafe(HolderSet<TF> hs , @MaybeNull TData data , Action2<TF , TData> action)
    {
        for (var feature : hs)
        {
            if (feature.isBound() == false)
            {
                MDEXBalmLayer.LOGGER.warn("FPUtils: Cannot access a feature required to be generated. Check your config and the feature path you have provided.");
                continue;
            }
            // Perform a check here whether this feature config is ours modded feature config,
            // and if so, check whether this config is invalid so that to not dispatch the action method.
            TF p = feature.value();
            if (p.feature().value().config() instanceof ModdedFeatureConfiguration mfc && mfc.isInvalid()) {
                continue;
            }
            action.action(p , data);
        }
    }

    // data1 and data2 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2> void ForEachInHolderSetUnsafe(HolderSet<TF> hs , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , Action3<TF , TData1 , TData2> action)
    {
        for (var feature : hs)
        {
            if (feature.isBound() == false)
            {
                MDEXBalmLayer.LOGGER.warn("FPUtils: Cannot access a feature required to be generated. Check your config and the feature path you have provided.");
                continue;
            }
            // Perform a check here whether this feature config is ours modded feature config,
            // and if so, check whether this config is invalid so that to not dispatch the action method.
            TF p = feature.value();
            if (p.feature().value().config() instanceof ModdedFeatureConfiguration mfc && mfc.isInvalid()) {
                continue;
            }
            action.action(p , data1 , data2);
        }
    }

    // data1, data2 and data3 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2 , TData3> void ForEachInHolderSetUnsafe(HolderSet<TF> hs , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , @MaybeNull TData3 data3 , Action4<TF , TData1 , TData2 , TData3> action)
    {
        for (var feature : hs)
        {
            if (feature.isBound() == false)
            {
                MDEXBalmLayer.LOGGER.warn("FPUtils: Cannot access a feature required to be generated. Check your config and the feature path you have provided.");
                continue;
            }
            // Perform a check here whether this feature config is ours modded feature config,
            // and if so, check whether this config is invalid so that to not dispatch the action method.
            TF p = feature.value();
            if (p.feature().value().config() instanceof ModdedFeatureConfiguration mfc && mfc.isInvalid()) {
                continue;
            }
            action.action(p , data1 , data2 , data3);
        }
    }



    // data parameter is a request dependent value, so it can be null if the caller does not require it.
    public static <TF extends PlacedFeature , TData> void ForEachInList(List<TF> list , @MaybeNull TData data , Action2<TF , TData> action)
    {
        ArgumentNullException.ThrowIfNull(list , "list");
        ArgumentNullException.ThrowIfNull(action , "action");
        ForEachInListUnsafe(list , data , action);
    }

    // data1 and data2 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2> void ForEachInList(List<TF> list , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , Action3<TF , TData1 , TData2> action)
    {
        ArgumentNullException.ThrowIfNull(list , "list");
        ArgumentNullException.ThrowIfNull(action , "action");
        ForEachInListUnsafe(list , data1 , data2 , action);
    }

    // data1, data2 and data3 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2 , TData3> void ForEachInList(List<TF> list , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , @MaybeNull TData3 data3 , Action4<TF , TData1 , TData2 , TData3> action)
    {
        ArgumentNullException.ThrowIfNull(list, "list");
        ArgumentNullException.ThrowIfNull(action, "action");
        ForEachInListUnsafe(list, data1, data2, data3, action);
    }

    // data parameter is a request dependent value, so it can be null if the caller does not require it.
    public static <TF extends PlacedFeature , TData> void ForEachInListUnsafe(List<TF> list , @MaybeNull TData data , Action2<TF , TData> action)
    {
        for (var feature : list)
        {
            // Perform a check here whether this feature config is ours modded feature config,
            // and if so, check whether this config is invalid so that to not dispatch the action method.
            if (feature.feature().value().config() instanceof ModdedFeatureConfiguration mfc && mfc.isInvalid()) {
                continue;
            }
            action.action(feature , data);
        }
    }

    // data1 and data2 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2> void ForEachInListUnsafe(List<TF> list , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , Action3<TF , TData1 , TData2> action)
    {
        for (var feature : list)
        {
            // Perform a check here whether this feature config is ours modded feature config,
            // and if so, check whether this config is invalid so that to not dispatch the action method.
            if (feature.feature().value().config() instanceof ModdedFeatureConfiguration mfc && mfc.isInvalid()) {
                continue;
            }
            action.action(feature , data1 , data2);
        }
    }

    // data1, data2 and data3 parameters are request dependent values, so they can be null if the caller does not require them.
    public static <TF extends PlacedFeature , TData1 , TData2 , TData3> void ForEachInListUnsafe(List<TF> list , @MaybeNull TData1 data1 , @MaybeNull TData2 data2 , @MaybeNull TData3 data3 , Action4<TF , TData1 , TData2 , TData3> action)
    {
        for (var feature : list)
        {
            // Perform a check here whether this feature config is ours modded feature config,
            // and if so, check whether this config is invalid so that to not dispatch the action method.
            if (feature.feature().value().config() instanceof ModdedFeatureConfiguration mfc && mfc.isInvalid()) {
                continue;
            }
            action.action(feature , data1 , data2 , data3);
        }
    }

    public static Predicate<BlockState> IsReplaceable(TagKey<Block> unreplaceable) {
        return (s) -> !s.is(unreplaceable);
    }

    public static Predicate<BlockState> IsReplaceableAndNotAir(TagKey<Block> unreplaceable)
    {
        return (BlockState b) -> !(b.is(Blocks.AIR) || b.is(Blocks.CAVE_AIR) || b.is(unreplaceable));
    }

    public static Predicate<BlockState> IsReplaceableOrAir(TagKey<Block> unreplaceable)
    {
        return (BlockState b) ->  (b.is(Blocks.AIR) || b.is(Blocks.CAVE_AIR)) || (!b.is(unreplaceable));
    }

    public static Predicate<BlockState> IsStrictlyAir()
    {
        return (BlockState b) -> b.getBlock() instanceof AirBlock;
    }

    public static boolean SafeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> oldState)
    {
        if (oldState.predicate(level.getBlockState(pos))) {
            return level.setBlock(pos, state, 2);
        }
        return false;
    }

    /**
     * Gets a random item from the specified list, and returns that item.
     * @param list The list of items to get a random item from.
     * @param source The random source to use for getting the random item.
     * @return The random item.
     * @param <T> The type of the items to select from. An item of such type is returned.
     * @deprecated Use {@link com.github.mdcdi1315.mdex.util.Extensions#SelectRandomFromListUnsafe(List, RandomSource)} instead.
     */
    @Deprecated(since = "1.3.0" , forRemoval = true)
    public static <T> T SampleFromRandomSource(List<T> list , RandomSource source)
    {
        return list.get(
                source.nextIntBetweenInclusive(0 , list.size() - 1)
        );
    }

    /**
     * Gets a random item from the specified list, and returns that item.
     * Additionally, it ensures that the specified item is not selected in any way.
     * @param list The list of items to get a random item from.
     * @param itemtoexclude The item instance to exclude from the possible outcomes.
     * @param source The random source to use for getting the random item.
     * @return The random item, ensuring that is not the instance specified in {@code itemtoexclude}.
     * @param <T> The type of the items to select from. An item of such type is returned.
     * @deprecated Use {@link com.github.mdcdi1315.mdex.util.Extensions#SelectRandomFromListWithExclusion(List, Object, RandomSource)} instead.
     */
    @Deprecated(since = "1.3.0" , forRemoval = true)
    public static <T> T SampleFromRandomSource(List<T> list , @MaybeNull T itemtoexclude , RandomSource source)
    {
        T item;
        int size = list.size() - 1;
        if (size == 0) {
            return list.get(0);
        }
        do {
            item = list.get(source.nextIntBetweenInclusive(0, size));
        } while (item.equals(itemtoexclude));
        return item;
    }

    public static @MaybeNull <T extends WeightedEntry> T SampleWeightedFromRandomSource(List<T> list , RandomSource rs)
    {
        // TODO: Optimize weighted calculations at some moment.
        return WeightedRandom.getRandomItem(rs, list).orElse(null);
    }

    public static void TrySpawnEntityAtChunkGenPhase(ServerLevelAccessor level , BlockPos finalpos , EntityType<?> type)
            throws ArgumentNullException
    {
        ArgumentNullException.ThrowIfNull(level , "level");
        ArgumentNullException.ThrowIfNull(finalpos , "finalpos");
        ArgumentNullException.ThrowIfNull(type , "type");
        TrySpawnEntityAtChunkGenPhaseInternal(level , finalpos , type);
    }

    private static void TrySpawnEntityAtChunkGenPhaseInternal(ServerLevelAccessor level , BlockPos finalpos , EntityType<?> type)
    {
        Entity ent;
        try {
            ent = type.create(level.getLevel());
        } catch (Exception e) {
            MDEXBalmLayer.LOGGER.warn("Failed to create mob instance." , e);
            return;
        }
        if (ent == null) {
            return;
        }

        ent.moveTo(finalpos.getX() , finalpos.getY() , finalpos.getZ());

        if (ent instanceof LivingEntity le)
        {
            if (le instanceof Mob m)
            {
                // Finalize the mob.
                RandomSource rs = level.getRandom();
                var attr = m.getAttribute(Attributes.FOLLOW_RANGE);
                if (attr != null) {
                    attr.addPermanentModifier(new AttributeModifier("Random spawn bonus", rs.triangle(0.0D, 0.11485000000000001), AttributeModifier.Operation.MULTIPLY_BASE));
                }
                m.setLeftHanded(rs.nextFloat() < 0.05F);
            }
            level.addFreshEntityWithPassengers(le);
        } else {
            MDEXBalmLayer.LOGGER.warn("Cannot spawn entity of type {} because it does not derive from LivingEntity. Skipping entity generation." , type.getClass().getName());
        }
    }

    /**
     * Places entities around the specified block position.
     * The entity to be spawned is selected once then it is randomly generated up to maxtimes parameter.
     * <p>
     *     Remarks: <br />
     *     The entities are placed on the world using the mob spawn type {@link MobSpawnType#CHUNK_GENERATION}.
     * </p>
     * @param level The {@link ServerLevelAccessor} object to apply the entities to.
     * @param basepos The block position where to place the entities to.
     * @param rs The {@link RandomSource} instance to use for randomization.
     * @param entityData A weighted list of entities to pick from.
     * @param maxtries Maximum spawn attempts to perform after an entity has been randomly picked out.
     * @throws ArgumentNullException <strong>level</strong> or <strong>basepos</strong> or <strong>entityData</strong> is <strong>null</strong>.
     * @throws ArgumentOutOfRangeException <strong>maxtries</strong> was negative.
     */
    public static void MakeTriggeredSpawns(ServerLevelAccessor level , BlockPos basepos , @MaybeNull RandomSource rs , List<WeightedEntityEntry> entityData , int maxtries)
            throws ArgumentNullException , ArgumentOutOfRangeException
    {
        ArgumentNullException.ThrowIfNull(level , "level");
        ArgumentNullException.ThrowIfNull(basepos , "basepos");
        ArgumentNullException.ThrowIfNull(entityData , "entityData");
        if (rs == null) {
            rs = level.getRandom();
        }
        if (maxtries < 0) {
            throw new ArgumentOutOfRangeException("maxtries" , "Maximum tries must not be a negative number!!!");
        }
        if (entityData.isEmpty()) { return; }
        var et = WeightedRandom.getRandomItem(rs, entityData);
        if (et.isPresent() == false) { return; }
        int mt = rs.nextIntBetweenInclusive(0 , maxtries);
        boolean flag = false;
        BlockPos finalpos;
        var entitytype = et.get();
        // TODO: Maybe add here a position randomizer to further avoid the entity cap issue.
        for (int I = 0; I < mt; I++)
        {
            if (flag) {
                finalpos = basepos.offset(-1 , 0 , 0);
                flag = false;
            } else {
                finalpos = basepos.offset(1 , 0 , 0);
                flag = true;
            }
            TrySpawnEntityAtChunkGenPhaseInternal(level, finalpos , entitytype.Entity);
        }
    }

    /**
     * Gets an iterator creating a rectangular area from the specified starting point and the offsets to apply to the original position.
     * @param starting The starting block position to start creating the rectangular area.
     * @param xyzoffset The X , Y and Z offset to apply from the starting position, specifying it's size.
     * @return An iterable of block positions that comprise the specified rectangular area.
     * @throws ArgumentNullException starting or xyzoffset were <strong>null</strong>.
     */
    public static Iterable<BlockPos> GetRectangularArea(BlockPos starting , BlockPos xyzoffset)
            throws ArgumentNullException {
        return new RectAreaIterable(starting , xyzoffset);
    }

    /**
     * Gets a value whether this placed feature instance has a configured feature a {@link ModdedFeature} instance, and whether that is invalid.
     * @param pf The {@link PlacedFeature} to test.
     * @return The value, indicating the test's success or not.
     */
    public static boolean IsInvalidModdedPlacedFeature(@MaybeNull PlacedFeature pf)
    {
        if (pf == null) { return false; }
        return IsInvalidModdedConfiguredFeatureUnsafe(pf.feature().value());
    }

    /**
     * Gets a value whether this configured feature instance is invalid.
     * @param cf The {@link ConfiguredFeature} to test.
     * @return The value, indicating the test's success or not.
     */
    public static boolean IsInvalidModdedConfiguredFeature(@MaybeNull ConfiguredFeature<? , ?> cf)
    {
        if (cf == null) { return false; }
        return IsInvalidModdedConfiguredFeatureUnsafe(cf);
    }

    private static boolean IsInvalidModdedConfiguredFeatureUnsafe(ConfiguredFeature<? , ?> cf)
    {
        return cf.config() instanceof ModdedFeatureConfiguration mfc && mfc.isInvalid();
    }
}
