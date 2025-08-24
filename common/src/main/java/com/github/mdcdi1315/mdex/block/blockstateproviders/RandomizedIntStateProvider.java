package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.Optional;

public class RandomizedIntStateProvider
        extends AbstractBlockStateProvider
{
    public final AbstractBlockStateProvider source;
    public final String propertyName;
    @MaybeNull
    public IntegerProperty property;
    public final IntProvider values;

    public RandomizedIntStateProvider(AbstractBlockStateProvider source, IntegerProperty property, IntProvider values) {
        this.source = source;
        this.property = property;
        this.propertyName = property.getName();
        this.values = values;
        Collection<Integer> collection = property.getPossibleValues();

        for (int i = values.getMinValue(); i <= values.getMaxValue(); ++i) {
            if (!collection.contains(i)) {
                String var10002 = property.getName();
                throw new IllegalArgumentException("Property value out of range: " + var10002 + ": " + i);
            }
        }

    }

    public RandomizedIntStateProvider(AbstractBlockStateProvider source, String propertyName, IntProvider values) {
        this.source = source;
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    protected AbstractBlockStateProviderType<?> type() {
        return CustomBlockStateProviderRegistrySubsystem.RANDOMIZED_INT_STATE_PROVIDER;
    }

    public BlockState getState(RandomSource random, BlockPos pos) {
        BlockState blockstate = this.source.getState(random, pos);
        if (this.property == null || !blockstate.hasProperty(this.property)) {
            this.property = findProperty(blockstate, this.propertyName);
        }

        return blockstate.setValue(this.property, this.values.sample(random));
    }

    private static IntegerProperty findProperty(BlockState state, String propertyName) {
        Collection<Property<?>> collection = state.getProperties();
        Optional<IntegerProperty> optional = collection.stream().filter((p_161583_) -> p_161583_.getName().equals(propertyName)).filter((p_161588_) -> p_161588_ instanceof IntegerProperty).map((p_161574_) -> (IntegerProperty)p_161574_).findAny();
        return optional.orElseThrow(() -> new IllegalArgumentException("Illegal property: " + propertyName));
    }

    @Override
    public void Compile() {
        source.Compile();
    }

    @Override
    public boolean IsCompiled() {
        return source.IsCompiled();
    }
}
