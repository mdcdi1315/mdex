package com.github.mdcdi1315.mdex.mixin;

import com.github.mdcdi1315.mdex.features.FeaturePlacementUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BiomeGenerationSettings.PlainBuilder.class)
public abstract class PlainBuilder_BiomeGenerationSettingsMixin
{
    @Accessor("features")
    protected abstract List<List<Holder<PlacedFeature>>> GetFeatures();

    @Unique
    private static boolean MDEX$REMOVALPREDICATE(Holder<PlacedFeature> pf)
    {
        return pf.isBound() && FeaturePlacementUtils.IsInvalidModdedPlacedFeature(pf.value());
    }

    @Final
    @Inject(at = @At("HEAD") , method = "build")
    public void MDEX$BUILDGENSETTINGS(CallbackInfoReturnable<BiomeGenerationSettings> returnable)
    {
        for (var p1 : GetFeatures()) {
            p1.removeIf(PlainBuilder_BiomeGenerationSettingsMixin::MDEX$REMOVALPREDICATE);
        }
    }
}
