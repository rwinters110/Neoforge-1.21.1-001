package net.rwinters110.lotrmod.inventory;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rwinters110.lotrmod.lotrmod;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, lotrmod.MOD_ID);

    public static final Supplier<MenuType<KegMenu>> KEG_MENU =
            MENUS.register("keg_menu", () -> IMenuTypeExtension.create(KegMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}