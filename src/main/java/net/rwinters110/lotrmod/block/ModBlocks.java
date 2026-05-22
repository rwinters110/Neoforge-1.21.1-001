package net.rwinters110.lotrmod.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rwinters110.lotrmod.block.custom.KegBlock;
import net.rwinters110.lotrmod.item.Moditems;
import net.rwinters110.lotrmod.lotrmod;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(lotrmod.MOD_ID);

    public static final DeferredBlock<Block> GONDOR_ROCK = registerBlock("gondor_rock",
            () -> new Block(BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> GONDOR_BRICKS = registerBlock("gondor_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MORDOR_ROCK = registerBlock("mordor_rock",
            () -> new Block(BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MORDOR_BRICKS = registerBlock("mordor_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new DropExperienceBlock(UniformInt.of(2,5),
                    BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MITHRIL_ORE = registerBlock("mithril_ore",
            () -> new DropExperienceBlock(UniformInt.of(5,10),
                    BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_MITHRIL_ORE = registerBlock("deepslate_mithril_ore",
            () -> new DropExperienceBlock(UniformInt.of(5,10),
                    BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
            () -> new DropExperienceBlock(UniformInt.of(6,12),
                    BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = registerBlock("raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> RAW_MITHRIL_BLOCK = registerBlock("raw_mithril_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4F).requiresCorrectToolForDrops()));
    // Registro de tu tarta de manzana usando tu clase personalizada
    public static final DeferredBlock<Block> APPLE_CRUMBLE = BLOCKS.register("apple_crumble",
            () -> new ModCakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 2, 0.75F));
    // Registro de tu tarta de cerezas usando tu clase personalizada
    public static final DeferredBlock<Block> CHERRY_PIE = BLOCKS.register("cherry_pie",
            () -> new ModCakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 2, 0.75F));
    // Registro de tu tarta de frutos del bosque usando tu clase personalizada
    public static final DeferredBlock<Block> BERRY_PIE = BLOCKS.register("berry_pie",
            () -> new ModCakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), 2, 0.75F));
    public static final DeferredBlock<Block> KEG = registerBlock("keg",
            () -> new KegBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        Moditems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
