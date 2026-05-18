package net.rwinters110.lotrmod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rwinters110.lotrmod.block.ModBlocks;
import net.rwinters110.lotrmod.lotrmod;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, lotrmod.MOD_ID);

    public static final Supplier<CreativeModeTab> MIDDLE_EARTH_BLOCKS_TAB =
            CREATIVE_MODE_TABS.register( "middle_earth_blocks_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable( "itemGroup.lotrmod.middle_earth_blocks_tab"))
                    .icon(() -> new ItemStack(ModBlocks.GONDOR_BRICKS.get()))
                    .displayItems((pParameters,pOutput) -> {
                        pOutput.accept(ModBlocks.GONDOR_ROCK);
                        pOutput.accept(ModBlocks.GONDOR_BRICKS);
                        pOutput.accept(ModBlocks.MORDOR_ROCK);
                        pOutput.accept(ModBlocks.MORDOR_BRICKS);
                        pOutput.accept(ModBlocks.TIN_ORE);
                        pOutput.accept(ModBlocks.DEEPSLATE_TIN_ORE);
                        pOutput.accept(ModBlocks.MITHRIL_ORE);
                        pOutput.accept(ModBlocks.DEEPSLATE_MITHRIL_ORE);
                        pOutput.accept(ModBlocks.RAW_TIN_BLOCK);
                        pOutput.accept(ModBlocks.RAW_MITHRIL_BLOCK);

                    })
                    .build());

    public static final Supplier<CreativeModeTab> MIDDLE_EARTH_MATERIAL_ITEMS_TAB =
            CREATIVE_MODE_TABS.register( "middle_earth_material_items_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable( "itemGroup.lotrmod.middle_earth_material_items_tab"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(lotrmod.MOD_ID, "middle_earth_blocks_tab"))
                    .icon(() -> new ItemStack(Moditems.TIN_INGOT.get()))
                    .displayItems((pParameters,pOutput) -> {
                        pOutput.accept(Moditems.RAW_TIN);
                        pOutput.accept(Moditems.TIN_INGOT);
                        pOutput.accept(Moditems.RAW_MITHRIL);
                        pOutput.accept(Moditems.MITHRIL_INGOT);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> MIDDLE_EARTH_ITEMS_MISCELLANEOUS_TAB =
            CREATIVE_MODE_TABS.register( "middle_earth_items_miscellaneous_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable( "itemGroup.lotrmod.middle_earth_items_miscellaneous_tab"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(lotrmod.MOD_ID, "middle_earth_blocks_tab"))
                    .icon(() -> new ItemStack(Moditems.GOLD_RING.get()))
                    .displayItems((pParameters,pOutput) -> {
                        pOutput.accept(Moditems.GOLD_RING);
                        pOutput.accept(Moditems.SILVER_RING);
                        pOutput.accept(Moditems.MITHRIL_RING);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> MIDDLE_EARTH_FOOD_TAB =
            CREATIVE_MODE_TABS.register( "middle_earth_food_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable( "itemGroup.lotrmod.middle_earth_food_tab"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(lotrmod.MOD_ID, "middle_earth_blocks_tab"))
                    .icon(() -> new ItemStack(Moditems.LEMBAS.get()))
                    .displayItems((pParameters,pOutput) -> {
                        pOutput.accept(Moditems.LEMBAS);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
