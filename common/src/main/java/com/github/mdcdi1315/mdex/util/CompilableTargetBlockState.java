package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CompilableTargetBlockState
    implements Compilable
{
    private static Codec<CompilableTargetBlockState> codec;
    public ResourceLocation Name;
    public BlockState BlockState;
    public Map<String , String> IProperties;
    public Map<Property<?> , Comparable<?>> Properties;

    public CompilableTargetBlockState(ResourceLocation id , Map<String , String> props)
    {
        Name = id;
        IProperties = props;
        Properties = null;
    }

    public static Codec<CompilableTargetBlockState> GetCodec()
    {
        if (codec == null)
        {
            codec = CodecUtils.CreateCodecDirect(
                    ResourceLocation.CODEC.fieldOf(net.minecraft.world.level.block.state.BlockState.NAME_TAG).forGetter((CompilableTargetBlockState se) -> se.Name),
                    Codec.unboundedMap(Codec.STRING , Codec.STRING).optionalFieldOf(net.minecraft.world.level.block.state.BlockState.PROPERTIES_TAG , Map.of()).forGetter((CompilableTargetBlockState se) -> se.IProperties),
                    CompilableTargetBlockState::new
            );
        }
        return codec;
    }

    public void Compile()
    {
        this.BlockState = BlockUtils.GetBlockFromID(Name).defaultBlockState();
        Name = null;
        Properties = new HashMap<>(IProperties.size() , 0.56f);
        for (var k : IProperties.keySet())
        {
            Property<?> prop;
            String value = IProperties.get(k);
            for (var f : BlockStateProperties.class.getFields())
            {
                if (Property.class.isAssignableFrom(f.getType()))
                {
                    try {
                        prop = (Property<?>) f.get(null);
                        if (prop.getName().equals(k))
                        {
                            Optional<?> any = prop.getValue(value);
                            if (any.isPresent()) {
                                Properties.put(prop , (Comparable<?>) any.get());
                            } else {
                                MDEXBalmLayer.LOGGER.warn("Cannot get the value of the block's property '{}' with ID '{}'. The value '{}' may be invalid. Not including it in the final property list." , k , Name.toDebugFileName() , value);
                            }
                        }
                    } catch (IllegalAccessException ae) {
                        MDEXBalmLayer.LOGGER.error("Cannot access the field named as {}. Fetching the next property." , f.getName());
                    }
                    break;
                }
            }
        }
        IProperties = null;
    }

    public boolean IsCompiled()
    {
        return Properties != null && BlockState != null;
    }
}
