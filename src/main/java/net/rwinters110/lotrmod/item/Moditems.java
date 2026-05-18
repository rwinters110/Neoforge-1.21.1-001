package net.rwinters110.lotrmod.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
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


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
