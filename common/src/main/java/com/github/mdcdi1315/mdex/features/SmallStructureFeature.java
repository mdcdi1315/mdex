package com.github.mdcdi1315.mdex.features;

import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.features.config.SmallStructureConfiguration;
import com.github.mdcdi1315.mdex.features.smallstructure.StructureElementConfig;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.*;

public final class SmallStructureFeature
    extends ModdedFeature<SmallStructureConfiguration>
{
    public SmallStructureFeature(Codec<SmallStructureConfiguration> codec)
    {
        super(codec);
    }

    private void GetCompiledTemplates(WorldGenLevel level , List<StructureElementConfig> configs)
    {
        StructureTemplateManager stm = level.getLevel().getServer().getStructureManager();
        for (int I = 0; I < configs.size(); I++)
        {
            var cfg = configs.get(I);
            // Check that the structure exists.
            Optional<StructureTemplate> temp = stm.get(cfg.StructureID);

            if (temp.isEmpty()) {
                MDEXBalmLayer.LOGGER.warn("Cannot load the structure with ID '{}' because it cannot be found. Removing it from structure list." , cfg.StructureID);
                configs.remove(I);
                I--;
            } else {
                cfg.Template = temp.get();
                Vec3i size = cfg.Template.getSize();
                if (size.getX() > 64 || size.getY() > 80 || size.getZ() > 64) {
                    MDEXBalmLayer.LOGGER.error("ERROR: Cannot generate the structure with ID '{}' because it is too big. This structure configuration will be ignored during this generation pass." , cfg.StructureID);
                    configs.remove(I);
                    I--;
                    continue;
                }
                cfg.StructureID = null;
            }
        }
    }

    @Override
    protected boolean placeModdedFeature(FeaturePlaceContext<SmallStructureConfiguration> fpc)
    {
        if (!fpc.config().TemplatesAreCompiled)
        {
            GetCompiledTemplates(fpc.level() , fpc.config().Structures);
            if (fpc.config().Structures.isEmpty()) {
                fpc.config().setConfigAsInvalid();
                return false;
            }
            fpc.config().TemplatesAreCompiled = true;
        }

        RandomSource rs = fpc.random();

        var cfg = fpc.config().Structures.get(rs.nextIntBetweenInclusive(0 , fpc.config().Structures.size() - 1));

        var temp = cfg.Template;
        var settings = cfg.Settings;
        var size = temp.getSize(settings.PlacementRotation);
        var position = temp.getZeroPositionWithTransform(fpc.origin() , settings.PlacementMirror , settings.PlacementRotation);

        // Complete the placement settings, preparing for placement.
        StructurePlaceSettings sets = new StructurePlaceSettings();
        sets.setRandom(rs);
        // Static settings defined directly through data
        sets.setRotation(settings.PlacementRotation);
        sets.setMirror(settings.PlacementMirror);
        sets.setIgnoreEntities(settings.ShouldIgnoreEntities);
        sets.setKeepLiquids(settings.ShouldKeepFluids);
        // Apply all the found processors
        for (var proc : fpc.config().StructuresProcessors.value().list())
        {
            sets.addProcessor(proc);
        }
        // Place the structure. Will fail if and only if the structure itself could not be placed.
        if (temp.placeInWorld(fpc.level() , position , position , sets , rs , 4))
        {
            // The structure has been placed, however we need to generate additional features on its contents.
            // As a starting point I will get the center of the structure and I will start throwing random calls
            // to the placed features.

            if (fpc.config().AdditionalFeatures.size() == 0)
            {
                // No additional features to generate, return directly.
                return true;
            }

            if (size.getY() < 4)
            {
                // Cannot generate features under a so small space
                return true;
            }

            size = size.offset(-(size.getX() / 2) , 0 , -(size.getZ() / 2));

            // Express the center of the structure as a reference point to hit random features
            BlockPos posbound = fpc.origin().offset(size) , bp;

            for (int I = fpc.random().nextIntBetweenInclusive(3 , size.getY()); I > 0; I--)
            {
                bp = posbound.offset(0 , fpc.random().nextIntBetweenInclusive(3 , size.getY()) , 0);
                // Hit all features for this pass, ensuring that all features are being tested on the same position.
                // If at least one is generated for this pass, all the others are skipped.
                for (var f : fpc.config().AdditionalFeatures)
                {
                    if (f.value().place(fpc.level() , fpc.chunkGenerator() , fpc.random() , bp)) {
                        break;
                    }
                }
            }

            return true;
        }
        return false;
    }
}
