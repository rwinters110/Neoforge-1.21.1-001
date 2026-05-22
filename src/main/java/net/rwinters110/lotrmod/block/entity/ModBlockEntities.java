package net.rwinters110.lotrmod.block.entity;

import net.neoforged.bus.api.IEventBus;
import net.rwinters110.lotrmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "lotrmod");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KegBlockEntity>> KEG =
            BLOCK_ENTITIES.register("keg", () ->
                    BlockEntityType.Builder.of(KegBlockEntity::new, ModBlocks.KEG.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
