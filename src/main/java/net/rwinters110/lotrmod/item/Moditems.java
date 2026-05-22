package net.rwinters110.lotrmod.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rwinters110.lotrmod.block.ModBlocks;
import net.rwinters110.lotrmod.item.custom.ModFuelItem;
import net.rwinters110.lotrmod.lotrmod;

public class Moditems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(lotrmod.MOD_ID);

    public static final DeferredItem<Item> GOLD_RING = ITEMS.registerSimpleItem( "gold_ring");
    public static final DeferredItem<Item> SILVER_RING = ITEMS.registerSimpleItem( "silver_ring");
    public static final DeferredItem<Item> MITHRIL_RING = ITEMS.registerSimpleItem( "mithril_ring");
    public static final DeferredItem<Item> RAW_TIN = ITEMS.registerSimpleItem( "raw_tin");
    public static final DeferredItem<Item> RAW_MITHRIL = ITEMS.registerSimpleItem( "raw_mithril");
    public static final DeferredItem<Item> TIN_INGOT = ITEMS.registerSimpleItem( "tin_ingot");
    public static final DeferredItem<Item> MITHRIL_INGOT = ITEMS.registerSimpleItem( "mithril_ingot");
    public static final DeferredItem<Item> LEMBAS =
            ITEMS.registerItem( "lembas", Item::new, new Item.Properties().food(ModFoodProperties.LEMBAS));
    // Registra la tarta como ítem para que se pueda rellenar el inventario y colocar en el mundo
    public static final DeferredItem<Item> APPLE_CRUMBLE = ITEMS.register("apple_crumble",
            () -> new BlockItem(ModBlocks.APPLE_CRUMBLE.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CHERRY_PIE = ITEMS.register("cherry_pie",
            () -> new BlockItem(ModBlocks.CHERRY_PIE.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BERRY_PIE = ITEMS.register("berry_pie",
            () -> new BlockItem(ModBlocks.BERRY_PIE.get(), new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DURNOR =
            ITEMS.registerItem( "durnor", properties -> new ModFuelItem(properties, 1600), new Item.Properties());
    public static final DeferredItem<Item> GREEN_APPLE =
            ITEMS.registerItem( "green_apple", Item::new, new Item.Properties().food(ModFoodProperties.GREEN_APPLE));

    // .stacksTo(1) asegura que no se puedan acumular, igual que las tartas vanilla


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
