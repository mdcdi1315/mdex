package com.github.mdcdi1315.mdex.block.blockstateproviders;

import com.github.mdcdi1315.DotNetLayer.System.ArgumentException;
import com.github.mdcdi1315.DotNetLayer.System.InvalidOperationException;
import com.github.mdcdi1315.DotNetLayer.System.Diagnostics.CodeAnalysis.MaybeNull;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.Collection;

public final class RandomizedIntStateProvider
    extends AbstractBlockStateProvider
{
    public IntProvider values;
    public String propertyName;
    @MaybeNull
    public IntegerProperty property;
    public AbstractBlockStateProvider source;

    public RandomizedIntStateProvider(AbstractBlockStateProvider source, IntegerProperty property, IntProvider values) {
        this.source = source;
        this.property = property;
        this.propertyName = property.getName();
        this.values = values;
    }

    public RandomizedIntStateProvider(AbstractBlockStateProvider source, String propertyName, IntProvider values) {
        this.source = source;
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    public BlockState GetBlockState(BlockStateProviderContext context) {
        BlockState blockstate = this.source.GetBlockState(context);
        if (this.property == null || !blockstate.hasProperty(this.property))
        {
            this.property = findProperty(blockstate, this.propertyName);
            ValidateProperty();
        }

        return blockstate.setValue(this.property, this.values.sample(context.source()));
    }

    private static IntegerProperty findProperty(BlockState state, String propertyName)
    {
        Collection<Property<?>> collection = state.getProperties();
        for (Property<?> p : collection)
        {
            if (propertyName.equals(p.getName()) && p instanceof IntegerProperty ip) {
                return ip;
            }
        }
        throw new InvalidOperationException("Illegal property: " + propertyName);
    }

    @Override
    public AbstractBlockStateProviderType<?> GetType() {
        return RandomizedIntStateProviderType.INSTANCE;
    }

    private void ValidateProperty()
    {
        Collection<Integer> collection = property.getPossibleValues();

        for (int i = values.getMinValue(); i <= values.getMaxValue(); ++i)
        {
            if (!collection.contains(i)) {
                DisposeFields();
                throw new ArgumentException(String.format("Property value out of range: %s: %d" , property.getName() , i));
            }
        }
    }

    private void DisposeFields()
    {
        propertyName = null;
        property = null;
        values = null;
    }

    @Override
    protected boolean CompileImplementation()
    {
        source.Compile();
        if (source.IsCompiled())
        {
            if (property != null) {
                ValidateProperty();
            }
            return true;
        } else {
            DisposeFields();
            return false;
        }
    }
}
