package com.github.mdcdi1315.mdex.structures.customizablemineshaft;

import com.github.mdcdi1315.mdex.codecs.CodecUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This settings object is used for encoding/decoding the NBT data for generating the structure itself. <br />
 * Thus, this object is NOT COMPILABLE, this has happened during compilation state. <br />
 * This just defines what Minecraft does actually need...
 */
public final class CustomizableMineshaftPiecesSettings
{
    public final BlockState PlanksState;
    public final BlockState FenceState;
    public final BlockState TorchState;
    public final BlockState WoodState;
    public final ResourceLocation MinecartsLootTable;
    public final float MinecartPlacementProbability;

    public static Codec<CustomizableMineshaftPiecesSettings> GetCodec()
    {
        // The passed block states will be already encoded and will be validated and OK, so we can use Minecraft's logic here.
        var c = BlockState.CODEC;
        return CodecUtils.CreateCodecDirect(
                c.fieldOf("planks_state").forGetter((sets) -> sets.PlanksState),
                c.fieldOf("fence_state").forGetter((sets) -> sets.FenceState),
                c.fieldOf("torch_state").forGetter((sets) -> sets.TorchState),
                c.fieldOf("wood_state").forGetter((sets) -> sets.WoodState),
                ResourceLocation.CODEC.fieldOf("minecart_loot_table").forGetter((sets) -> sets.MinecartsLootTable),
                // PERF: We have already validated the probability, just allow the value to be passed directly
                Codec.FLOAT.fieldOf("minecart_placement_probability").forGetter((sets) -> sets.MinecartPlacementProbability),
                CustomizableMineshaftPiecesSettings::new
        );
    }

    public CustomizableMineshaftPiecesSettings(
        BlockState plank_state,
        BlockState fence_state,
        BlockState torch_state,
        BlockState wood_state,
        ResourceLocation minecart_loot_table,
        float p
    )
    {
        PlanksState = plank_state;
        FenceState = fence_state;
        TorchState = torch_state;
        WoodState = wood_state;
        MinecartsLootTable = minecart_loot_table;
        MinecartPlacementProbability = p;
    }
}
