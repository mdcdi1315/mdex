package com.github.mdcdi1315.mdex.util;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.BlockUtils;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;

public final class CompilableTargetBlockState
    implements Compilable
{
    private static Codec<CompilableTargetBlockState> codec;
    private ResourceLocation Name;
    public BlockState BlockState;
    private Map<String , String> IProperties;

    public CompilableTargetBlockState(ResourceLocation id , Map<String , String> props)
    {
        Name = id;
        IProperties = props;
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
        Method m = null;
        for (var md : getClass().getDeclaredMethods())
        {
            if (md.getName().equals("SetPropValue") && (md.getModifiers() & (Modifier.PRIVATE | Modifier.STATIC)) != 0)
            {
                m = md;
                break;
            }
        }
        if (m == null)
        {
            MDEXBalmLayer.LOGGER.error("CompilableTargetBlockState: Cannot compile block state because the method 'SetPropValue' method was not found.");
            this.BlockState = null;
            return;
        }
        for (var k : IProperties.keySet())
        {
            String value = IProperties.get(k);
            for (Property<?> prop : BlockState.getProperties())
            {
                try {
                    if (prop.getName().equals(k))
                    {
                        Optional<?> any = prop.getValue(value);
                        if (any.isPresent()) {
                            BlockState = (BlockState) m.invoke(null , BlockState , prop , any.get());
                        } else {
                            MDEXBalmLayer.LOGGER.warn("Cannot get the value of the block's property '{}' with ID '{}'. The value '{}' may be invalid. Not including it in the final property list." , k , Name , value);
                        }
                    }
                } catch (InvocationTargetException ite) {
                    if (ite.getTargetException() instanceof IllegalArgumentException iae) {
                        MDEXBalmLayer.LOGGER.warn("Cannot set the value of the specified block state with ID '{}' to '{}'.", Name, value , iae);
                    } else {
                        MDEXBalmLayer.LOGGER.error("Exception occurred during reflective invocation." , ite.getTargetException());
                    }
                } catch (IllegalAccessException iae) {
                    MDEXBalmLayer.LOGGER.error("Exception occurred during reflective invocation." , iae);
                    break;
                }
            }
        }
        Name = null;
        IProperties = null;
    }

    // We need to statically reference the setValue method of BlockState so that the obfuscated method is called.
    @SuppressWarnings("unused") // Used by reflection
    private static <T extends Comparable<T>, V extends T> BlockState SetPropValue(BlockState bs, Property<T> p , V c)
    {
        return bs.setValue(p , c);
    }

    public boolean IsCompiled()
    {
        return BlockState != null;
    }
}
