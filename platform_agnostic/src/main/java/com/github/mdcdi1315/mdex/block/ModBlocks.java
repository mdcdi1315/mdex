package com.github.mdcdi1315.mdex.block;

import com.github.mdcdi1315.basemodslib.block.IBlockRegistrar;
import com.github.mdcdi1315.basemodslib.block.entity.IBlockEntityFactory;
import com.github.mdcdi1315.basemodslib.block.BlockRegistrationInformation;
import com.github.mdcdi1315.basemodslib.block.entity.IBlockEntityRegistrar;
import com.github.mdcdi1315.basemodslib.eventapi.mods.ModLoadingCompleteEvent;

import com.github.mdcdi1315.mdex.MDEXModInstance;
import com.github.mdcdi1315.mdex.block.entity.TeleporterTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;


public final class ModBlocks
{
    private ModBlocks() {}

    public static Block TELEPORTER;

    public static BlockEntityType<TeleporterTileEntity> TELEPORTER_TILE_ENTITY;



    public static void Initialize(IBlockRegistrar blocks)
    {
        blocks.Register("teleporter", new BlockRegistrationInformation(
                (identifier) -> TELEPORTER = new MDEXTeleporterBlock(
                        BlockBehaviour.Properties.of()
                                .strength(5.233455f , 4.2f)
                                .sound(SoundType.METAL)
                                .requiresCorrectToolForDrops()
                                .mapColor(MapColor.METAL)
                ),
                ModBlocks::GetBlockItem,
                BlockUtils.GetMinecraftCreativeModeTab("functional_blocks")
        ));

        InitializeHardstoneBlockFamily(blocks);
        InitializeDeepGraniteBlockFamily(blocks);
    }

    private static void InitializeHardstoneBlockFamily(IBlockRegistrar blocks)
    {
        CreativeModeTab natural_blocks = BlockUtils.GetMinecraftCreativeModeTab("natural_blocks");
        CreativeModeTab building_blocks = BlockUtils.GetMinecraftCreativeModeTab("building_blocks");

        blocks.Register("hardstone", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseBlock(
                                BlockBehaviour.Properties.of()
                                        .strength(4.1843755f , 3.4f)
                                        .sound(SoundType.STONE)
                                        .requiresCorrectToolForDrops()
                                        .mapColor(MapColor.STONE)
                        ),
                        ModBlocks::GetBlockItem,
                        natural_blocks
                )
        );

        blocks.Register("hardstone_bricks", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseBlock(
                                BlockBehaviour.Properties.of()
                                        .strength(3.8494383f , 3.4f)
                                        .sound(SoundType.STONE)
                                        .requiresCorrectToolForDrops()
                                        .mapColor(MapColor.STONE)
                        ),
                        ModBlocks::GetBlockItem,
                        building_blocks
                )
        );

        blocks.Register("hardstone_brick_stairs", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseStairBlock(
                                // We cannot directly access right now the hardstone bricks reference, so we must instruct
                                // the builder to load it on the fly.
                                // Elsewise, this will be called only once.
                                BlockUtils.GetBlockFromID(
                                        ResourceLocation.tryBuild(MDEXModInstance.MOD_ID , "hardstone_bricks")
                                ).defaultBlockState(),
                                BlockBehaviour.Properties.of()
                                        .strength(3.644758445f , 3.4f)
                                        .sound(SoundType.STONE)
                                        .requiresCorrectToolForDrops()
                                        .mapColor(MapColor.STONE)
                        ),
                        ModBlocks::GetBlockItem,
                        building_blocks
                )
        );

        blocks.Register("hardstone_brick_slab",new BlockRegistrationInformation(
                    (ResourceLocation identifier) -> new MDEXBaseSlabBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(3.274367455f , 3.289f)
                                    .sound(SoundType.STONE)
                                    .requiresCorrectToolForDrops()
                                    .mapColor(MapColor.STONE)
                    ),
                    ModBlocks::GetBlockItem,
                    building_blocks
                )
        );

        InitializeHardstoneOres(blocks, natural_blocks);
    }

    private static void InitializeDeepGraniteBlockFamily(IBlockRegistrar blocks)
    {
        blocks.Register("granite", new BlockRegistrationInformation(
                    (ResourceLocation identifier) -> new MDEXBaseBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(3.14784563f , 2.84f)
                                    .sound(SoundType.STONE)
                                    .requiresCorrectToolForDrops()
                                    .mapColor(MapColor.STONE)
                    ),
                    ModBlocks::GetBlockItem
                )
        );

        blocks.Register("polished_granite", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseBlock(
                                BlockBehaviour.Properties.of()
                                        .strength(2.774633f , 2.84f)
                                        .sound(SoundType.STONE)
                                        .requiresCorrectToolForDrops()
                                        .mapColor(MapColor.STONE)
                        ),
                        ModBlocks::GetBlockItem
                )
        );

        blocks.Register("polished_granite_stairs", new BlockRegistrationInformation(
                    (ResourceLocation identifier) -> new MDEXBaseStairBlock(
                            BlockUtils.GetBlockFromID(MDEXModInstance.BlockID("polished_granite")).defaultBlockState(),
                            BlockBehaviour.Properties.of()
                                    .strength(2.759384f , 2.84f)
                                    .sound(SoundType.STONE)
                                    .requiresCorrectToolForDrops()
                                    .mapColor(MapColor.STONE)
                    ),
                    ModBlocks::GetBlockItem
                )
        );

        blocks.Register("polished_granite_slab", new BlockRegistrationInformation(
                    (ResourceLocation identifier) -> new MDEXBaseSlabBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(2.72049f , 2.7103f)
                                    .sound(SoundType.STONE)
                                    .requiresCorrectToolForDrops()
                                    .mapColor(MapColor.STONE)
                    ),
                    ModBlocks::GetBlockItem
                )
        );
    }

    private static void InitializeHardstoneOres(IBlockRegistrar blocks, CreativeModeTab natural_blocks)
    {
        blocks.Register("hardstone_redstone_ore", new BlockRegistrationInformation(
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
                    natural_blocks
                )
        );

        blocks.Register("hardstone_iron_ore", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                                BlockBehaviour.Properties.of()
                                        .requiresCorrectToolForDrops()
                                        .strength(4.18F , 3.45f)
                        ),
                        ModBlocks::GetBlockItem,
                        natural_blocks
                )
        );

        blocks.Register("hardstone_copper_ore", new BlockRegistrationInformation(
                (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                        BlockBehaviour.Properties.of()
                                .requiresCorrectToolForDrops()
                                .strength(4.1745F , 3.45f)
                ),
                ModBlocks::GetBlockItem,
                natural_blocks
            )
        );

        blocks.Register("hardstone_emerald_ore", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                                BlockBehaviour.Properties.of()
                                        .requiresCorrectToolForDrops()
                                        .strength(4.7774F , 3.45F)
                        ),
                        ModBlocks::GetBlockItem,
                        natural_blocks
                )
        );

        blocks.Register("hardstone_gold_ore", new BlockRegistrationInformation(
                    (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                            BlockBehaviour.Properties.of()
                                    .requiresCorrectToolForDrops()
                                    .strength(4.19008484f , 3.45f)
                    ),
                    ModBlocks::GetBlockItem
                )
        );

        blocks.Register("hardstone_diamond_ore", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                                BlockBehaviour.Properties.of()
                                        .requiresCorrectToolForDrops()
                                        .strength(4.8f , 3.45f),
                                UniformInt.of(3 , 7)
                        ),
                        ModBlocks::GetBlockItem,
                        natural_blocks
                )
        );

        blocks.Register("hardstone_lapis_ore", new BlockRegistrationInformation(
                        (ResourceLocation identifier) -> new MDEXBaseOreBlock(
                                BlockBehaviour.Properties.of()
                                        .requiresCorrectToolForDrops()
                                        .strength(4.612f , 3.45f),
                                UniformInt.of(2 , 5)
                        ),
                        ModBlocks::GetBlockItem,
                        natural_blocks
                )
        );
    }

    private static BlockItem GetBlockItem(Block block , ResourceLocation identifier)
    {
        MDEXModInstance.LOGGER.trace("Registering block item named as {}" , identifier);
        return new BlockItem(block, new Item.Properties());
    }

    private static final class TeleporterBlockEntityFactory
        implements IBlockEntityFactory<TeleporterTileEntity>
    {
        @Override
        public TeleporterTileEntity Create(BlockPos blockPos, BlockState blockState) {
            return new TeleporterTileEntity(blockPos , blockState);
        }

        @Override
        public Block[] GetBlocks() {
            return new Block[] { TELEPORTER };
        }
    }

    public static void InitBlockEntities(IBlockEntityRegistrar blockentities) {
        blockentities.Register("teleporter", new TeleporterBlockEntityFactory());
    }

    public static void OnModLoadingComplete(ModLoadingCompleteEvent e) {
        TELEPORTER_TILE_ENTITY = IBlockEntityRegistrar.GetBlockEntityType(MDEXModInstance.BlockID("teleporter"));
    }
}
