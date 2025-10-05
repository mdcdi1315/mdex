package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.DotNetLayer.System.ObjectDisposedException;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public final class CompilableBlockState
    implements Compilable
{
    private static Codec<CompilableBlockState> codec;
    private ResourceLocation Name;
    public BlockState BlockState;
    private Map<String , String> IProperties;

    public CompilableBlockState(ResourceLocation id , Map<String , String> props)
    {
        Name = id;
        IProperties = props;
    }

    public static Codec<CompilableBlockState> GetCodec()
    {
        if (codec == null)
        {
            codec = CodecUtils.CreateCodecDirect(
                    ResourceLocation.CODEC.fieldOf(net.minecraft.world.level.block.state.BlockState.NAME_TAG).forGetter(CompilableBlockState::GetId),
                    Codec.unboundedMap(Codec.STRING , Codec.STRING).optionalFieldOf(net.minecraft.world.level.block.state.BlockState.PROPERTIES_TAG , Map.of()).forGetter(CompilableBlockState::GetPropertyMap),
                    CompilableBlockState::new
            );
        }
        return codec;
    }

    public static MapCodec<CompilableBlockState> GetMapCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                ResourceLocation.CODEC.fieldOf(net.minecraft.world.level.block.state.BlockState.NAME_TAG).forGetter(CompilableBlockState::GetId),
                Codec.unboundedMap(Codec.STRING , Codec.STRING).optionalFieldOf(net.minecraft.world.level.block.state.BlockState.PROPERTIES_TAG , Map.of()).forGetter(CompilableBlockState::GetPropertyMap),
                CompilableBlockState::new
        );
    }

    public void Compile()
    {
        try {
            // Get the block's default state. With this state we will work on and set all the other properties that the user requires.
            this.BlockState = BlockUtils.GetBlockFromID(Name).defaultBlockState();
            PropertySettingData tdata;
            for (var ent : IProperties.entrySet())
            {
                String key = ent.getKey();
                String value = ent.getValue();
                for (Property<?> prop : BlockState.getProperties())
                {
                    if (key.equals(prop.getName())) // this might be less error-prone
                    {
                        tdata = SetPropertyValue(BlockState, prop, value);
                        if (tdata.Succeeded) {
                            // If the desired value was set, update our block state.
                            BlockState = tdata.AssignedState;
                        } else {
                            MDEXBalmLayer.LOGGER.warn("Cannot set the value of the block's property '{}' with ID '{}' to '{}'. The value may be invalid or invalid for this block definition. Not including it in the final property list.", key, Name, value);
                        }
                        break; // OPT - We do not need to check for other properties, we have done our purpose
                    }
                }
            }
        } finally {
            // Destroy unneeded fields
            Name = null;
            IProperties = null;
        }
    }

    // A small record class to retrieve information whether the set property value operation happening in the SetPropertyValue
    // method has been completed successfully or not, and if yes, it returns the new block state that has the updated property value.
    private static class PropertySettingData
    {
        public BlockState AssignedState;
        public boolean Succeeded;
    }

    // TODO: Generalize this API, make it public
    private static <T extends Comparable<T>> PropertySettingData SetPropertyValue(BlockState bs , Property<T> prop , String propvalas_string)
    {
        PropertySettingData d = new PropertySettingData();
        Optional<T> opt = prop.getValue(propvalas_string);
        d.Succeeded = opt.isPresent();
        if (d.Succeeded) {
            try {
                d.AssignedState = bs.setValue(prop, opt.get());
            } catch (IllegalArgumentException iae) {
                // It can be only thrown when the passed value is not supported for the block definition.
                // However, I am doing this to not just immediately destroy a DCO or something similar -
                // the error will be logged however, thus the developer will be aware of the mistake.
                d.Succeeded = false;
            }
        }
        return d;
    }

    private ResourceLocation GetId()
    {
        ObjectDisposedException.ThrowIf(BlockState == null , this);
        return BuiltInRegistries.BLOCK.getKey(BlockState.getBlock());
    }

    private Map<String , String> GetPropertyMap()
    {
        ObjectDisposedException.ThrowIf(BlockState == null , this);
        var ps = BlockState.getProperties();
        HashMap<String , String> s = new HashMap<>(ps.size());
        for (var p : ps)
        {
            s.put(p.getName() , BlockState.getValue(p).toString());
        }
        return s;
    }

    public boolean IsCompiled()
    {
        return BlockState != null;
    }
}
