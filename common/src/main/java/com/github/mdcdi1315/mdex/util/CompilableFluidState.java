package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ObjectDisposedException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

/**
 * A class providing compilable fluid states like the {@link CompilableBlockState} class does.
 */
public final class CompilableFluidState
    implements Compilable
{
    private static Codec<CompilableFluidState> codec;
    public FluidState FluidState;
    private ResourceLocation FluidID;
    private Map<String , String> IProperties;

    public CompilableFluidState(ResourceLocation fluidId , Map<String , String> props)
    {
        FluidID = fluidId;
        IProperties = props;
        FluidState = null;
    }

    public static Codec<CompilableFluidState> GetCodec()
    {
        if (codec == null)
        {
            codec = CodecUtils.CreateCodecDirect(
                    ResourceLocation.CODEC.fieldOf(net.minecraft.world.level.material.FluidState.NAME_TAG).forGetter(CompilableFluidState::GetId),
                    Codec.unboundedMap(Codec.STRING , Codec.STRING).optionalFieldOf(net.minecraft.world.level.material.FluidState.PROPERTIES_TAG , Map.of()).forGetter(CompilableFluidState::GetPropertyMap),
                    CompilableFluidState::new
            );
        }
        return codec;
    }

    public static MapCodec<CompilableFluidState> GetMapCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                ResourceLocation.CODEC.fieldOf(net.minecraft.world.level.material.FluidState.NAME_TAG).forGetter(CompilableFluidState::GetId),
                Codec.unboundedMap(Codec.STRING , Codec.STRING).optionalFieldOf(net.minecraft.world.level.material.FluidState.PROPERTIES_TAG , Map.of()).forGetter(CompilableFluidState::GetPropertyMap),
                CompilableFluidState::new
        );
    }

    @Override
    public void Compile() {
        try {
            this.FluidState = BlockUtils.GetFluidFromID(FluidID).defaultFluidState();
            PropertySettingData tdata;
            for (var entry : IProperties.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();
                for (Property<?> prop : FluidState.getProperties())
                {
                    if (key.equals(prop.getName())) // this might be less error-prone
                    {
                        tdata = SetPropertyValue(FluidState, prop, value);
                        if (tdata.Succeeded) {
                            // If the desired value was set, update our fluid state.
                            FluidState = tdata.AssignedState;
                        } else {
                            MDEXBalmLayer.LOGGER.warn("Cannot set the value of the fluid's property '{}' with ID '{}' to '{}'. The value may be invalid or invalid for this block definition. Not including it in the final property list.", key, FluidID, value);
                        }
                        break; // OPT - We do not need to check for other properties, we have done our purpose
                    }
                }
            }
        } finally {
            FluidID = null;
            IProperties = null;
        }
    }

    // A small record class to retrieve information whether the set property value operation happening in the SetPropertyValue
    // method has been completed successfully or not, and if yes, it returns the new block state that has the updated property value.
    private static class PropertySettingData
    {
        public FluidState AssignedState;
        public boolean Succeeded;
    }

    // TODO: Generalize this API, make it public
    private static <T extends Comparable<T>> PropertySettingData SetPropertyValue(FluidState bs , Property<T> prop , String propvalas_string)
    {
        PropertySettingData d = new PropertySettingData();
        Optional<T> opt = prop.getValue(propvalas_string);
        d.Succeeded = opt.isPresent();
        if (d.Succeeded) {
            try {
                d.AssignedState = bs.setValue(prop, opt.get());
            } catch (IllegalArgumentException iae) {
                // It can be only thrown when the passed value is not supported for the fluid definition.
                // However, I am doing this to not just immediately destroy a DCO or something similar -
                // the error will be logged however, thus the developer will be aware of the mistake.
                d.Succeeded = false;
            }
        }
        return d;
    }

    private ResourceLocation GetId()
    {
        ObjectDisposedException.ThrowIf(FluidState == null , this);
        return BuiltInRegistries.FLUID.getKey(FluidState.getType());
    }

    private Map<String , String> GetPropertyMap()
    {
        ObjectDisposedException.ThrowIf(FluidState == null , this);
        var ps = FluidState.getProperties();
        HashMap<String , String> s = new HashMap<>(ps.size());
        for (var p : ps)
        {
            s.put(p.getName() , FluidState.getValue(p).toString());
        }
        return s;
    }

    @Override
    public boolean IsCompiled() {
        return FluidState != null;
    }
}
