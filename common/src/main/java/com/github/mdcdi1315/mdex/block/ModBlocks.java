package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.mdex.MDEXBalmLayer;
import com.github.mdcdi1315.mdex.block.entity.TeleporterTileEntity;

import net.minecraft.util.valueproviders.UniformInt;

import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.block.BalmBlockEntities;

import net.minecraft.world.level.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;


public final class ModBlocks
{
    private ModBlocks() {}

    public static final String CREATIVE_BUILDING_BLOCKS_TAB = "building_blocks";

    public static final String CREATIVE_FUNCTIONAL_BLOCKS_TAB = "functional_blocks";

    public static final String CREATIVE_NATURAL_BLOCKS_TAB = "natural_blocks";

    public static Block TELEPORTER;

    public static DeferredObject<BlockEntityType<TeleporterTileEntity>> TELEPORTER_TILE_ENTITY;

    public static void Initialize(BalmBlocks blocks)
    {
        blocks.register((identifier) -> TELEPORTER = new MDEXTeleporterBlock(
                    BlockBehaviour.Properties.of()
                            .strength(5.233455f , 4.2f)
                            .sound(SoundType.METAL)
                            .requiresCorrectToolForDrops()
                            .mapColor(MapColor.METAL)
            ),
            ModBlocks::GetBlockItem ,
            MDEXBalmLayer.BlockID("teleporter") ,
            ResourceLocation.tryParse(CREATIVE_FUNCTIONAL_BLOCKS_TAB)
        );

        InitializeHardstoneBlockFamily(blocks);
        InitializeDeepGraniteBlockFamily(blocks);
    }

    private static void InitializeHardstoneBlockFamily(BalmBlocks blocks)
    {
        ResourceLocation buildingblockstab = ResourceLocation.tryParse(CREATIVE_BUILDING_BLOCKS_TAB);
        ResourceLocation naturalblockstab = ResourceLocation.tryParse(CREATIVE_NATURAL_BLOCKS_TAB);

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseBlock(
                        BlockBehaviour.Properties.of()
                                .strength(4.1843755f , 3.4f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem ,
                MDEXBalmLayer.BlockID("hardstone") ,
                naturalblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseBlock(
                        BlockBehaviour.Properties.of()
                                .strength(3.8494383f , 3.4f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_bricks"),
                buildingblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseStairBlock(
                        // We cannot directly access right now the hardstone bricks reference, so we must instruct
                        // the builder to load it on the fly.
                        // Elsewise, this will be called only once.
                        BlockUtils.GetBlockFromID(
                                ResourceLocation.tryBuild(MDEXBalmLayer.MODID , "hardstone_bricks")
                        ).defaultBlockState(),
                        BlockBehaviour.Properties.of()
                                .strength(3.644758445f , 3.4f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_brick_stairs"),
                buildingblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseSlabBlock(
                        BlockBehaviour.Properties.of()
                                .strength(3.274367455f , 3.289f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_brick_slab"),
                buildingblockstab
        );

        InitializeHardstoneOres(blocks , naturalblockstab);
    }

    private static void InitializeDeepGraniteBlockFamily(BalmBlocks blocks)
    {
        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseBlock(
                        BlockBehaviour.Properties.of()
                                .strength(3.14784563f , 2.84f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("granite"),
                ResourceLocation.tryParse(CREATIVE_NATURAL_BLOCKS_TAB)
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseBlock(
                        BlockBehaviour.Properties.of()
                                .strength(2.774633f , 2.84f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("polished_granite"),
                ResourceLocation.tryParse(CREATIVE_BUILDING_BLOCKS_TAB)
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseStairBlock(
                        BlockUtils.GetBlockFromID(
                                ResourceLocation.tryBuild(MDEXBalmLayer.MODID , "polished_granite")
                        ).defaultBlockState(),
                        BlockBehaviour.Properties.of()
                                .strength(2.759384f , 2.84f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("polished_granite_stairs"),
                ResourceLocation.tryParse(CREATIVE_BUILDING_BLOCKS_TAB)
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseSlabBlock(
                        BlockBehaviour.Properties.of()
                                .strength(2.72049f , 2.7103f)
                                .sound(SoundType.STONE)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.STONE)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("polished_granite_slab"),
                ResourceLocation.tryParse(CREATIVE_BUILDING_BLOCKS_TAB)
        );
    }

    private static void InitializeHardstoneOres(BalmBlocks blocks, ResourceLocation naturalblockstab)
    {
        blocks.register(
                (ResourceLocation identifier) -> new RedStoneOreBlock(
                    BlockBehaviour.Properties.of()
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .sound(SoundType.STONE)
                            .mapColor(MapColor.STONE)
                            .randomTicks()
                            .lightLevel(BlockUtils.GetBlockLightEmissionWhenLit(9))
                            .requiresCorrectToolForDrops()
                            .strength(4.27F, 3.45F)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_redstone_ore"),
                naturalblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                        BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(4.18F , 3.45f)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_iron_ore"),
                naturalblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                        BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(4.1745F , 3.45f)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_copper_ore"),
                naturalblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                        BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(4.7774F , 3.45F)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_emerald_ore"),
                naturalblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                        BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(4.19008484f , 3.45f)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_gold_ore"),
                naturalblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                        BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(4.8f , 3.45f),
                        UniformInt.of(3 , 7)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_diamond_ore"),
                naturalblockstab
        );

        blocks.register(
                (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                        BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(4.612f , 3.45f),
                        UniformInt.of(2 , 5)
                ),
                ModBlocks::GetBlockItem,
                MDEXBalmLayer.BlockID("hardstone_lapis_ore"),
                naturalblockstab
        );
    }

    private static BlockItem GetBlockItem(Block block , ResourceLocation identifier)
    {
        MDEXBalmLayer.LOGGER.trace("Registering block item named as {}" , identifier);
        return new BlockItem(block, BalmItems.itemProperties(identifier));
    }

    public static void InitBlockEntities(BalmBlockEntities blockentities)
    {
        TELEPORTER_TILE_ENTITY = blockentities.registerBlockEntity(MDEXBalmLayer.BlockID("teleporter"), TeleporterTileEntity::new, () -> new Block[]{TELEPORTER});
    }
}
