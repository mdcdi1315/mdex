package com.github.mdcdi1315.mdex.structures.customizablemineshaft;

import com.github.mdcdi1315.mdex.util.Compilable;
import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.github.mdcdi1315.mdex.util.CompilableBlockState;
import com.github.mdcdi1315.mdex.structures.customizablemineshaft.pieces.MineshaftPieces;

import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

public final class CustomizableMineshaftStructureSettings
    implements Compilable
{
    public CompilableBlockState PlanksState;
    public CompilableBlockState FenceState;
    public CompilableBlockState TorchState;
    public CompilableBlockState WoodState;
    public ResourceLocation MinecartsLootTable;
    public final float MinecartPlacementProbability;
    public HeightProvider Starting_Y_Level;
    private boolean compiled;

    public static MapCodec<CustomizableMineshaftStructureSettings> GetCodec()
    {
        return CodecUtils.CreateMapCodecDirect(
                CompilableBlockState.GetCodec().fieldOf("planks_state").forGetter((s) -> s.PlanksState),
                CompilableBlockState.GetCodec().fieldOf("fence_state").forGetter(s -> s.FenceState),
                CompilableBlockState.GetCodec().fieldOf("torch_state").forGetter(s -> s.TorchState),
                CompilableBlockState.GetCodec().fieldOf("wood_state").forGetter(s -> s.WoodState),
                ResourceLocation.CODEC.optionalFieldOf("minecart_loot_table" , BuiltInLootTables.ABANDONED_MINESHAFT).forGetter(s -> s.MinecartsLootTable),
                HeightProvider.CODEC.optionalFieldOf("starting_y_level" , ConstantHeight.of(VerticalAnchor.absolute(MineshaftPieces.MAGIC_START_Y))).forGetter((s) -> s.Starting_Y_Level),
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
            HeightProvider starting_y_level,
            float p
    )
    {
        PlanksState = planks_state;
        FenceState = fence_state;
        TorchState = torch_state;
        WoodState = wood_state;
        MinecartsLootTable = minecart_loot_table;
        MinecartPlacementProbability = p;
        Starting_Y_Level = starting_y_level;
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

    /**
     * Creates a new {@link CustomizableMineshaftPiecesSettings} object to be used for serializing the structure data during partial structure save.
     * @return The data that can be used to reconstruct the structure later.
     */
    public CustomizableMineshaftPiecesSettings CreateSettingsObject()
    {
        return new CustomizableMineshaftPiecesSettings(
                PlanksState.BlockState,
                FenceState.BlockState,
                TorchState.BlockState,
                WoodState.BlockState,
                MinecartsLootTable,
                MinecartPlacementProbability
        );
    }
}
