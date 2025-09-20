package com.github.mdcdi1315.mdex.structures.customizablemineshaft;


import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public final class CustomizableMineshaftStructureSettings
    implements Compilable
{
    public CompilableBlockState PlanksState;
    public CompilableBlockState FenceState;
    public CompilableBlockState TorchState;
    public CompilableBlockState WoodState;
    public ResourceLocation MinecartsLootTable;
    public final float MinecartPlacementProbability;
    private boolean compiled;

    public static MapCodec<CustomizableMineshaftStructureSettings> GetCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                CompilableBlockState.GetCodec().fieldOf("planks_state").forGetter((s) -> s.PlanksState),
                CompilableBlockState.GetCodec().fieldOf("fence_state").forGetter(s -> s.FenceState),
                CompilableBlockState.GetCodec().fieldOf("torch_state").forGetter(s -> s.TorchState),
                CompilableBlockState.GetCodec().fieldOf("wood_state").forGetter(s -> s.WoodState),
                ResourceLocation.CODEC.optionalFieldOf("minecart_loot_table" , BuiltInLootTables.ABANDONED_MINESHAFT.location()).forGetter(s -> s.MinecartsLootTable),
                CodecUtils.FLOAT_PROBABILITY.optionalFieldOf("minecart_placement_probability" , 0.01f).forGetter(s -> s.MinecartPlacementProbability),
                CustomizableMineshaftStructureSettings::new
        );
    }

    public CustomizableMineshaftStructureSettings(
            CompilableBlockState planks_state,
            CompilableBlockState fence_state,
            CompilableBlockState torch_state,
            CompilableBlockState wood_state,
            ResourceLocation minecart_loot_table,
            float p
    )
    {
        PlanksState = planks_state;
        FenceState = fence_state;
        TorchState = torch_state;
        WoodState = wood_state;
        MinecartsLootTable = minecart_loot_table;
        MinecartPlacementProbability = p;
    }

    public void Compile()
    {
        compiled = false;
        PlanksState.Compile();
        if (!PlanksState.IsCompiled()) {
            return;
        }
        FenceState.Compile();
        if (!FenceState.IsCompiled()) {
            return;
        }
        TorchState.Compile();
        if (!TorchState.IsCompiled()) {
            return;
        }
        WoodState.Compile();
        if (!WoodState.IsCompiled()) {
            return;
        }
        compiled = true;
    }

    public boolean IsCompiled()
    {
        return compiled;
    }
}
