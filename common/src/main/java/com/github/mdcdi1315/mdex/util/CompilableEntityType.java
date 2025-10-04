package com.github.mdcdi1315.mdex.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Optional;

public final class CompilableEntityType
    implements Compilable
{
    private static Codec<CompilableEntityType> codec;
    public EntityType<?> Entity;
    private ResourceLocation location;

    private CompilableEntityType()
    {
        Entity = null;
        location = null;
    }

    private static DataResult<CompilableEntityType> Create(ResourceLocation location)
    {
        CompilableEntityType cet = new CompilableEntityType();
        cet.location = location;
        return DataResult.success(cet);
    }

    private static DataResult<ResourceLocation> GetId(CompilableEntityType entity)
    {
        // Will always succeed.
        return DataResult.success(BuiltInRegistries.ENTITY_TYPE.getKey(entity.Entity));
    }

    public static Codec<CompilableEntityType> GetCodec()
    {
        if (codec == null)
        {
            codec = ResourceLocation.CODEC.flatXmap(CompilableEntityType::Create , CompilableEntityType::GetId);
        }
        return codec;
    }

    @Override
    public void Compile()
    {
        try {
            Optional<EntityType<?>> o = BuiltInRegistries.ENTITY_TYPE.getOptional(location);
            if (o.isEmpty()) {
                throw new EntityTypeNotFoundException(location);
            }
            Entity = o.get();
        } finally {
            location = null;
        }
    }

    @Override
    public boolean IsCompiled() {
        return Entity != null;
    }
}
